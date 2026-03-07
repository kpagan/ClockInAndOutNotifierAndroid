package com.kpagan.clockinandoutnotifier.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.kpagan.clockinandoutnotifier.data.ClockInAndOutRepositoryImpl
import com.kpagan.clockinandoutnotifier.services.ClockInAndOutBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ClockInAndOutManager {

    fun registerGeofence(context: Context) {

        val repo = ClockInAndOutRepositoryImpl(context)

        CoroutineScope(Dispatchers.IO).launch {
            val config = repo.getConfig()

            val geofence = Geofence.Builder()
                .setRequestId("target")
                .setCircularRegion(
                    config.latitude,
                    config.longitude,
                    config.radius
                )
                .setTransitionTypes(
                    Geofence.GEOFENCE_TRANSITION_ENTER or
                            Geofence.GEOFENCE_TRANSITION_EXIT
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()

            val request = GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build()

            val intent = Intent(context, ClockInAndOutBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            LocationServices.getGeofencingClient(context)
                .addGeofences(request, pendingIntent)
        }
    }
}