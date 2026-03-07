package com.kpagan.clockinandoutnotifier.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kpagan.clockinandoutnotifier.utils.ClockInAndOutManager
import com.kpagan.clockinandoutnotifier.utils.NotificationHelper

class ClockInAndOutForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForeground(1, NotificationHelper.foregroundNotification(this))
        ClockInAndOutManager.registerGeofence(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        RestartWorker.schedule(this)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}