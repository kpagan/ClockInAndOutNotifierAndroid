package com.kpagan.clockinandoutnotifier.services

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
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

            when (event.geofenceTransition) {

                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    NotificationHelper.alert(context, "Entered area")
                    open(context, config.siteUrl)
                }

                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    NotificationHelper.alert(context, "Exited area")
                    open(context, config.siteUrl)
                }
            }
        }
    }

    private fun open(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}