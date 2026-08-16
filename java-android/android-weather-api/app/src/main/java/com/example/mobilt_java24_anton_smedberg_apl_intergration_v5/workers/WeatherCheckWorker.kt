// workers/WeatherCheckWorker.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.workers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.NotificationUtils
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.ServiceLocator
import com.example.timelib.Thresholds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Går igenom sparade städer och skickar notis om regnrisk är hög.
 * Körs periodiskt via WorkManager, men kan även triggas manuellt (test/”kör nu”).
 */
class WeatherCheckWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork() = withContext(Dispatchers.IO) {
        val ctx = applicationContext
        val repo = ServiceLocator.repository()
        val settings = ServiceLocator.settingsStore()

        // ---- Testflaggor (från Settings) ----
        val bypassAlerts = inputData.getBoolean(KEY_BYPASS_ALERTS_CHECK, false)
        val sendAll = inputData.getBoolean(KEY_SEND_ALL_SAVED, false)
        val bypassCooldown = inputData.getBoolean(KEY_BYPASS_COOLDOWN, false)
        val forcedProb = inputData.getInt(KEY_FORCE_PCT, -1).takeIf { it >= 0 }

        // ---- Respektera användarens val (om inte bypassats) ----
        val alertsEnabled = runCatching { settings.alertsEnabledFlow.first() }.getOrDefault(false)
        if (!bypassAlerts && !alertsEnabled) {
            Log.d(TAG, "Vädervarningar är avstängda – hoppar över.")
            return@withContext Result.success()
        }

        // ---- Hämta sparade städer ----
        val favorites = runCatching { repo.listSaved() }.getOrDefault(emptyList())
        if (favorites.isEmpty()) {
            Log.d(TAG, "Inga sparade städer – klart.")
            return@withContext Result.success()
        }
        val targets = if (sendAll) favorites else listOf(favorites.first())

        // Skapa kanal defensivt (kräver ej POST_NOTIFICATIONS)
        NotificationUtils.createChannels(ctx)

        val threshold = Thresholds.RAIN_PROB_THRESHOLD

        for (city in targets) {
            // ---- Hämta prognos (retry på transienta fel) ----
            val snap = try {
                repo.getForecastFor(city)
            } catch (e: HttpException) {
                val retry = e.code() in 500..599
                Log.w(TAG, "HTTP ${e.code()} (${e.message()}), retry=$retry")
                return@withContext if (retry) Result.retry() else Result.success()
            } catch (e: IOException) {
                Log.w(TAG, "Nätverks/IO-fel (${e.message}) – retry")
                return@withContext Result.retry()
            } catch (t: Throwable) {
                Log.w(TAG, "Oväntat fel: ${t.javaClass.simpleName}: ${t.message} – hoppar över", t)
                return@withContext Result.success()
            }

            // ---- Välj sannolikhet: tvingad (test) eller “nästa timme” ----
            val rainProbNextHour = forcedProb ?: snap.nextHourRainProb
            Log.d(TAG, "City=${city.name} nextHour=$rainProbNextHour (threshold=$threshold)")

            // ---- Cooldown: undvik spam ----
            if (!bypassCooldown && !allowNotify(ctx)) {
                Log.d(TAG, "Cooldown aktiv – ingen notis för ${city.name}")
                continue
            }

            // Under tröskel? Gör inget för den staden
            if (rainProbNextHour < threshold) continue

            // ---- Kolla POST_NOTIFICATIONS (Android 13+) innan vi postar ----
            if (!canPostNotifications(ctx)) {
                Log.w(TAG, "Saknar POST_NOTIFICATIONS – kan inte visa notiser.")
                // Markera inte som notifierad; låt nästa körning försöka igen om tillstånd ges.
                continue
            }

            // ---- Skicka notis (säkra mot SecurityException) ----
            val sent = try {
                NotificationUtils.notifyRain(ctx, city.name, rainProbNextHour)
            } catch (se: SecurityException) {
                Log.w(TAG, "SecurityException vid notify – saknar rättighet?", se)
                false
            }

            Log.d(TAG, "notifyRain city=${city.name}, prob=$rainProbNextHour, sent=$sent")
            if (sent && !bypassCooldown) markNotified(ctx)
        }

        Result.success()
    }

    companion object {
        private const val TAG = "WeatherCheckWorker"

        // Unika work-namn
        private const val UNIQUE_PERIODIC = "weather-checker"
        private const val UNIQUE_ONCE = "weather-checker-once"

        // Cooldown (anti-spam)
        private const val COOL_DOWN_HOURS = 2L
        private const val COOL_DOWN_MS = COOL_DOWN_HOURS * 60 * 60 * 1000
        private const val PREFS = "weather_worker_prefs"
        private const val KEY_LAST_NOTIFY_TS = "last_notify_ts"

        // Input-nycklar (test)
        private const val KEY_BYPASS_COOLDOWN = "bypass_cooldown"
        private const val KEY_FORCE_PCT = "force_pct"
        private const val KEY_SEND_ALL_SAVED = "send_all_saved"
        private const val KEY_BYPASS_ALERTS_CHECK = "bypass_alerts_check"

        /** Schemalägg periodisk körning (var 3:e timme, 30 min flex). */
        fun schedule(context: Context) {
            val ctx = context.applicationContext

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<WeatherCheckWorker>(
                3, TimeUnit.HOURS,
                30, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }

        /** Kör en gång “nu” (produktion). */
        fun runOnce(context: Context) {
            enqueueOnce(
                context = context,
                bypassCooldown = false,
                forcePct = null,
                sendAll = false,
                bypassAlertsCheck = false
            )
        }

        /** Kör en gång “nu” (test). */
        fun runOnceTest(
            context: Context,
            forcePct: Int? = 85,
            sendAll: Boolean = true,
            bypassCooldown: Boolean = true,
            bypassAlertsCheck: Boolean = true
        ) {
            enqueueOnce(context, bypassCooldown, forcePct, sendAll, bypassAlertsCheck)
        }

        /** Avbryt både periodiska och enstaka jobb. */
        fun cancel(context: Context) {
            val wm = WorkManager.getInstance(context.applicationContext)
            wm.cancelUniqueWork(UNIQUE_PERIODIC)
            wm.cancelUniqueWork(UNIQUE_ONCE)
        }

        /** Rensa cooldown. Returnerar true om det fanns något att rensa. */
        fun clearCooldown(ctx: Context): Boolean {
            val sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val had = sp.contains(KEY_LAST_NOTIFY_TS)
            sp.edit { remove(KEY_LAST_NOTIFY_TS) }
            return had
        }

        /** Senaste utskickstid (ms), 0 om ingen. */
        fun lastNotifyTimestamp(ctx: Context): Long {
            val sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            return sp.getLong(KEY_LAST_NOTIFY_TS, 0L)
        }

        // ---- Interna hjälpare ----

        private fun enqueueOnce(
            context: Context,
            bypassCooldown: Boolean,
            forcePct: Int?,
            sendAll: Boolean,
            bypassAlertsCheck: Boolean
        ) {
            val ctx = context.applicationContext

            val base = workDataOf(
                KEY_BYPASS_COOLDOWN to bypassCooldown,
                KEY_SEND_ALL_SAVED to sendAll,
                KEY_BYPASS_ALERTS_CHECK to bypassAlertsCheck
            )
            val data: Data = if (forcePct != null) {
                Data.Builder().putAll(base).putInt(KEY_FORCE_PCT, forcePct).build()
            } else {
                Data.Builder().putAll(base).build()
            }

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val once = OneTimeWorkRequestBuilder<WeatherCheckWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(ctx).enqueueUniqueWork(
                UNIQUE_ONCE,
                ExistingWorkPolicy.REPLACE,
                once
            )
        }

        /** Får vi posta notiser? (Android 13+ kräver POST_NOTIFICATIONS) */
        private fun canPostNotifications(ctx: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= 33) {
                ActivityCompat.checkSelfPermission(
                    ctx, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        }

        /** Är cooldown passerad? */
        private fun allowNotify(ctx: Context): Boolean {
            val sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val last = sp.getLong(KEY_LAST_NOTIFY_TS, 0L)
            val now = System.currentTimeMillis()
            return (now - last) >= COOL_DOWN_MS
        }

        /** Markera att vi nyss skickade notis (starta cooldown). */
        private fun markNotified(ctx: Context) {
            val sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            sp.edit { putLong(KEY_LAST_NOTIFY_TS, System.currentTimeMillis()) }
        }
    }
}