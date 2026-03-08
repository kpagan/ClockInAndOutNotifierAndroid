package com.kpagan.clockinandoutnotifier.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    private const val CHANNEL = "ClockInAndOut"

    fun foregroundNotification(context: Context): Notification {
        createChannel(context)
        return NotificationCompat.Builder(context, CHANNEL)
            .setContentTitle("ClockInAndOut Active")
            .setContentText("Monitoring location")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun alert(context: Context, text: String, pendingIntent: PendingIntent? = null) {
        createChannel(context)
        val n = NotificationCompat.Builder(context, CHANNEL)
            .setContentTitle("ClockInAndOut")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(2, n)
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL,
            "ClockInAndOut",
            NotificationManager.IMPORTANCE_HIGH
        )
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

}
