// util/NotificationUtils.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.MainActivity
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.R

/**
 * Hjälpare för kanaler och själva regn-notisen.
 */
object NotificationUtils {

    const val CHANNEL_ID_WEATHER = "weather_alerts"
    private const val NOTIF_ID_RAIN = 1001

    /** Skapa kanal om den saknas (OK att kalla flera gånger). */
    fun createChannels(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(CHANNEL_ID_WEATHER) != null) return

        val name = context.getString(R.string.channel_weather_alerts)
        val desc = context.getString(R.string.channel_weather_alerts_desc)
        val channel = NotificationChannel(
            CHANNEL_ID_WEATHER,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = desc
            setShowBadge(true)
            enableVibration(true)
        }
        nm.createNotificationChannel(channel)
    }

    /**
     * Skicka “det kan börja regna”-notis.
     * @return true om skickad, annars false (t.ex. saknas POST_NOTIFICATIONS).
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notifyRain(context: Context, cityName: String, probabilityPct: Int): Boolean {
        if (!hasPostPermission(context)) return false

        // Skapa rimlig back stack till MainActivity
        val contentIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(Intent(context, MainActivity::class.java))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val title = context.getString(R.string.notify_title)
        val text = context.getString(
            R.string.notify_text_rain_city_pct,
            cityName,
            probabilityPct
        )

        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID_WEATHER)
            .setSmallIcon(R.drawable.ic_stat_rain)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // < API 26
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_RAIN, notification)
        return true
    }

    private fun hasPostPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 33) return true
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}