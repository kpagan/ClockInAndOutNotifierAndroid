package com.kpagan.clockinandoutnotifier.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.kpagan.clockinandoutnotifier.utils.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.kpagan.clockinandoutnotifier.data.ClockInAndOutRepositoryImpl

class ClockInAndOutBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val event = GeofencingEvent.fromIntent(intent) ?: return
        if (event.hasError()) return

        val repo = ClockInAndOutRepositoryImpl(context)

        CoroutineScope(Dispatchers.IO).launch {

            val config = repo.getConfig()

            val browserIntent = Intent(Intent.ACTION_VIEW, config.siteUrl.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                browserIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            when (event.geofenceTransition) {

                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    NotificationHelper.alert(context, "Entered area", pendingIntent)
                }

                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    NotificationHelper.alert(context, "Exited area", pendingIntent)
                }
            }
        }
    }
}