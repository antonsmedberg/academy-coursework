package com.example.mobilt_java24_anton_smedberg_shakev4;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

/**
 * Shake Rocket Lab
 * Jag håller standardvyn lugn. När jag behöver nörda ner mig visar jag detaljer i panelen.
 */
public class MainActivity extends Activity implements SensorEventListener {

    // —— Min verktygslåda (konstanter jag vill ha samlade) ——
    private static final float  G_EARTH          = 9.80665f;
    private static final float  LUX_UI_MAX       = 2000f;   // 0..2000 lux → mörk→ljus bakgrund
    private static final float  MAX_G_UI         = 5.0f;    // 5 g = full progress
    private static final float  ROLL_SMOOTH      = 0.22f;   // smidig ACC-rotation (0..1)

    private static final float  NOISE_G          = 0.05f;   // bort med mikroskopiskt brus
    private static final float  MIN_THRESHOLD_G  = 0.30f;   // lägsta tillåten tröskel
    private static final float  HYSTERESIS_G     = 0.15f;   // släppgräns efter trigger
    private static final long   TOAST_COOLDOWN   = 2200L;   // undvik toast-spam

    // —— Sensorer ——
    private SensorManager sensorMgr;
    private Sensor accSensor, gyroSensor, lightSensor;

    // —— Vyer jag pratar med ofta ——
    private View root, detailsPanel;
    private ImageView rocket;
    private ProgressBar boostBar;
    private SeekBar thresholdSeek;
    private SwitchCompat swGyro;
    private TextView btnToggleDetails;

    private TextView txtThresh, txtAccel, txtGyro, txtLight, txtStatus;
    private TextView chipShake, chipMode, chipLight;

    // —— Mitt tillstånd ——
    // Jag fördelar gravitation (low-pass) och linjär acceleration (high-pass)
    private final float[] grav = new float[]{0f,0f,0f};
    private final float[] lin  = new float[]{0f,0f,0f};
    private float lowPassAlpha = 0.82f;

    private float thresholdG = 2.7f;
    private long  lastToastAt = 0L;

    private boolean rotateWithGyro = false;
    private float accRollDegSmooth = 0f;
    private float yawDegIntegrated = 0f;
    private long  lastGyroTsNs     = 0L;

    private long  lastAccelUiAt = 0L;
    private float peakG = 0f;
    private int   shakeCount = 0;

    // “anti-spam”
    private boolean shakeArmed = true;
    private Toast sharedToast;

    // —— Demo (långtryck på raketen) ——
    private boolean demoOn   = false;
    private long    demoT0Ms = 0L;

    private final Runnable demoLoop = new Runnable() {
        @Override public void run() {
            if (!demoOn) return;

            // Jag kör ~60 fps
            final long now = SystemClock.elapsedRealtime();
            final float t = (now - demoT0Ms) / 1000f;

            // ACC: lite sväng och en kort “peak” ibland för att testa shake
            float ax = (float)( 2.0 * Math.sin(t * 1.3));
            float ay = (float)(-3.0 * Math.cos(t * 0.9));
            float az = 9.0f; // nära vilogravitation
            if (((int)t) % 2 == 0 && (t % 2f) < 0.08f) { ax += 12f; ay += 8f; }
            onAccel(ax, ay, az);

            // GYRO: mjuk wobble + långsam yaw (rad/s)
            float wx = (float)(0.35 * Math.sin(t * 0.7));
            float wy = (float)(0.30 * Math.cos(t * 0.5));
            float wz = (float)(0.55 * Math.sin(t * 0.35));
            onGyroSim(wx, wy, wz); // ~16 ms steg

            root.postDelayed(this, 16);
        }
    };

    private final Runnable statusTick = new Runnable() {
        @Override public void run() {
            updateStatusLine();
            root.postDelayed(this, 1000);
        }
    };

    // —— Livscykel ——
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        bindViews();
        initSensors();
        initUi();

        if (state != null) {
            peakG            = state.getFloat("peakG", 0f);
            shakeCount       = state.getInt("shakeCount", 0);
            yawDegIntegrated = state.getFloat("integratedYawDeg", 0f);
            rotateWithGyro   = state.getBoolean("useGyroToRotate", false);
            swGyro.setChecked(rotateWithGyro);
        }

        // startvärden
        thresholdG = Math.max(MIN_THRESHOLD_G, thresholdSeek.getProgress() / 10f);
        txtThresh.setText(getString(R.string.thresh_val_fmt, thresholdG));

        tintBgByLux(200f);     // neutral ton
        updateSummaryChips(0f, 200f);
        updateStatusLine();
    }

    @Override protected void onResume() {
        super.onResume();
        registerListeners();
        root.post(statusTick);
    }

    @Override protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(this);
        stopDemo();
        root.removeCallbacks(statusTick);
        if (sharedToast != null) { sharedToast.cancel(); sharedToast = null; }
    }

    @Override protected void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        out.putFloat ("peakG",            peakG);
        out.putInt   ("shakeCount",       shakeCount);
        out.putFloat ("integratedYawDeg", yawDegIntegrated);
        out.putBoolean("useGyroToRotate", rotateWithGyro);
    }

    // —— Setup ——
    private void bindViews() {
        root            = findViewById(R.id.root);
        rocket          = findViewById(R.id.ivRocket);
        boostBar        = findViewById(R.id.pbBoost);
        thresholdSeek   = findViewById(R.id.sbThreshold);
        swGyro          = findViewById(R.id.swGyro);
        detailsPanel    = findViewById(R.id.panelDetails);
        btnToggleDetails= findViewById(R.id.tvToggleDetails);

        txtThresh   = findViewById(R.id.tvThreshold);
        txtAccel    = findViewById(R.id.tvAccel);
        txtGyro     = findViewById(R.id.tvGyro);
        txtLight    = findViewById(R.id.tvLight);
        txtStatus   = findViewById(R.id.tvStatus);
        chipShake   = findViewById(R.id.tvShakeLevel);
        chipMode    = findViewById(R.id.tvMode);
        chipLight   = findViewById(R.id.tvLightLevel);
    }

    private void initSensors() {
        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorMgr == null) {
            Toast.makeText(this, "SensorManager saknas", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        accSensor   = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor  = sensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        lightSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    private void initUi() {
        // tröskel – jag visar värdet direkt och ser till att vi aldrig går under miniminivån
        thresholdSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar sb, int p, boolean fromUser) {
                thresholdG = Math.max(MIN_THRESHOLD_G, p / 10f);
                txtThresh.setText(getString(R.string.thresh_val_fmt, thresholdG));
                updateStatusLine();
            }
            @Override public void onStartTrackingTouch(SeekBar sb) { }
            @Override public void onStopTrackingTouch(SeekBar sb)  { }
        });

        // gyro-läge – om jag vill låta gyrot bestämma raketens rotation
        swGyro.setOnCheckedChangeListener((v, checked) -> {
            rotateWithGyro = checked;
            updateStatusLine();
            updateSummaryChips(null, null);
        });

        // visa/dölj detaljer – standardvyn hålls lugn
        btnToggleDetails.setOnClickListener(v -> {
            boolean hide = detailsPanel.getVisibility() == View.VISIBLE;
            detailsPanel.setVisibility(hide ? View.GONE : View.VISIBLE);
            btnToggleDetails.setText(hide ? R.string.toggle_details_show : R.string.toggle_details_hide);
        });

        // demo – långtryck på raketen
        rocket.setOnLongClickListener(v -> {
            if (demoOn) {
                stopDemo();
                Toast.makeText(this, R.string.demo_off, Toast.LENGTH_SHORT).show();
            } else {
                startDemo();
                Toast.makeText(this, R.string.demo_on, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void registerListeners() {
        boolean accOk = accSensor  != null && sensorMgr.registerListener(this, accSensor,  SensorManager.SENSOR_DELAY_GAME);
        boolean gyrOk = gyroSensor != null && sensorMgr.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
        boolean lgtOk = lightSensor!= null && sensorMgr.registerListener(this, lightSensor,SensorManager.SENSOR_DELAY_UI);

        if (!accOk) txtStatus.setText(R.string.err_no_accel);
        if (!gyrOk) txtGyro.setText(R.string.err_no_gyro);
        if (!lgtOk) txtLight.setText(R.string.err_no_light);
    }

    // —— SensorEventListener ——
    @Override public void onSensorChanged(SensorEvent e) {
        int type = e.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER && !demoOn) {
            onAccel(e.values[0], e.values[1], e.values[2]);
        } else if (type == Sensor.TYPE_GYROSCOPE && !demoOn) {
            onGyro(e.values[0], e.values[1], e.values[2], e.timestamp);
        } else if (type == Sensor.TYPE_LIGHT) {
            onLight(e.values[0]);
        }
    }
    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) { /* inget jag behöver här */ }

    // —— ACC → UI ——
    private void onAccel(float ax, float ay, float az) {
        lastAccelUiAt = SystemClock.elapsedRealtime();

        // low-pass → gravitation, high-pass → linjär acceleration
        grav[0] = lowPassAlpha * grav[0] + (1 - lowPassAlpha) * ax;
        grav[1] = lowPassAlpha * grav[1] + (1 - lowPassAlpha) * ay;
        grav[2] = lowPassAlpha * grav[2] + (1 - lowPassAlpha) * az;

        lin[0] = ax - grav[0];
        lin[1] = ay - grav[1];
        lin[2] = az - grav[2];

        // g-nivå jag använder som “skak-styrka”
        float g = (float) Math.sqrt(lin[0]*lin[0] + lin[1]*lin[1] + lin[2]*lin[2]) / G_EARTH;
        if (g < NOISE_G) g = 0f;
        peakG = Math.max(peakG, g);

        // progressbar
        int pct = (int) (100f * clamp01(g / MAX_G_UI));
        boostBar.setProgress(pct, true);

        // robust shake: min-tröskel + hysteresis + cooldown
        final float up   = Math.max(thresholdG, MIN_THRESHOLD_G);
        final float down = Math.max(up - HYSTERESIS_G, NOISE_G);
        long now = SystemClock.elapsedRealtime();
        boolean cool = (now - lastToastAt) > TOAST_COOLDOWN;

        if (shakeArmed && g > up && cool) {
            shakeArmed = false;
            lastToastAt = now;
            shakeCount++;

            if (sharedToast != null) sharedToast.cancel();
            sharedToast = Toast.makeText(this, getString(R.string.toast_shake, g), Toast.LENGTH_SHORT);
            sharedToast.show();

            root.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            updateStatusLine();
        } else if (!shakeArmed && g < down) {
            shakeArmed = true;
        }

        // rotation via ACC (om jag inte är i gyro-läge)
        if (!rotateWithGyro) {
            double rawRoll = Math.toDegrees(Math.atan2(ay, az));
            accRollDegSmooth = lerp(accRollDegSmooth, (float) -rawRoll);
            rocket.setRotation(wrapDeg(accRollDegSmooth));
        }

        // liten “thrust”-feedback för känsla
        float thrust = clamp01(g / 3f);
        rocket.setScaleX(1f + 0.02f * thrust);
        rocket.setScaleY(1f + 0.02f * thrust);
        rocket.setTranslationY(-6f * thrust);

        // läslig detaljtext (g-värden), layouten kör monospace
        float axg = lin[0] / G_EARTH, ayg = lin[1] / G_EARTH, azg = lin[2] / G_EARTH;
        txtAccel.setText(getString(R.string.accel_live_pretty_fmt, axg, ayg, azg, g, peakG));

        updateSummaryChips(g, null);
    }

    // —— GYRO → UI (riktiga events) ——
    private void onGyro(float wx, float wy, float wz, long tsNs) {
        if (lastGyroTsNs != 0L) {
            float dt = (tsNs - lastGyroTsNs) / 1_000_000_000f;
            yawDegIntegrated += (float) Math.toDegrees(wz * dt);
            yawDegIntegrated  = wrapDeg(yawDegIntegrated);
        }
        lastGyroTsNs = tsNs;

        txtGyro.setText(getString(R.string.gyro_live_pretty_fmt, wx, wy, wz, yawDegIntegrated));
        if (rotateWithGyro) rocket.setRotation(yawDegIntegrated);
    }

    // —— GYRO → UI (demo) ——
    private void onGyroSim(float wx, float wy, float wz) {
        yawDegIntegrated += (float) Math.toDegrees(wz * (float) 0.016);
        yawDegIntegrated  = wrapDeg(yawDegIntegrated);

        txtGyro.setText(getString(R.string.gyro_live_pretty_fmt, wx, wy, wz, yawDegIntegrated));
        if (rotateWithGyro) rocket.setRotation(yawDegIntegrated);
    }

    // —— Ljus → tema ——
    private void onLight(float lux) {
        txtLight.setText(getString(R.string.light_live_pretty_fmt, lux));
        tintBgByLux(lux);
        updateSummaryChips(null, lux);
    }

    private void tintBgByLux(float lux) {
        float t = clamp01(lux / LUX_UI_MAX);
        int v = (int) (0x16 + (0x24 - 0x16) * t);
        int tone = 0xFF000000 | (v << 16) | (v << 8) | v;
        root.setBackgroundColor(tone);
    }

    // —— Sammanfattning + status ——
    private void updateSummaryChips(Float gMaybe, Float luxMaybe) {
        if (gMaybe != null) {
            String label = (gMaybe < 0.6f) ? getString(R.string.shake_calm)
                    : (gMaybe < 1.5f) ? getString(R.string.shake_shaky)
                    : getString(R.string.shake_wild);
            chipShake.setText(getString(R.string.summary_shake_fmt, label));
        }

        String mode = rotateWithGyro ? getString(R.string.mode_gyro) : getString(R.string.mode_accel);
        chipMode.setText(getString(R.string.summary_mode_fmt, mode));

        if (luxMaybe != null) {
            String lightLabel = (luxMaybe < 80f) ? getString(R.string.light_dark)
                    : (luxMaybe < 600f) ? getString(R.string.light_indoor)
                    : getString(R.string.light_bright);
            chipLight.setText(getString(R.string.summary_light_fmt, lightLabel));
        }
    }

    private void updateStatusLine() {
        long idleMs = SystemClock.elapsedRealtime() - lastAccelUiAt;
        if (lastAccelUiAt == 0L || idleMs > 1500) {
            txtStatus.setText(R.string.status_no_motion);
        } else {
            txtStatus.setText(getString(
                    R.string.status_simple_fmt,
                    rotateWithGyro ? getString(R.string.mode_gyro) : getString(R.string.mode_accel),
                    thresholdG, shakeCount, peakG
            ));
        }
    }

    // —— Demo ——
    private void startDemo() {
        if (demoOn) return;
        demoOn = true;
        demoT0Ms = SystemClock.elapsedRealtime();
        root.post(demoLoop);
        updateStatusLine();
    }

    private void stopDemo() {
        demoOn = false;
        root.removeCallbacks(demoLoop);
        updateStatusLine();
    }

    // —— Små hjälpare ——
    private static float clamp01(float v) { return Math.max(0f, Math.min(1f, v)); }

    private static float lerp(float a, float b) { return a + (b - a) * MainActivity.ROLL_SMOOTH; }

    private static float wrapDeg(float d) {
        d %= 360f;
        if (d > 180f) d -= 360f;
        if (d < -180f) d += 360f;
        return d;
    }
}