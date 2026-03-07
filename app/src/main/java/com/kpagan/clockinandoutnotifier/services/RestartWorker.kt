package com.kpagan.clockinandoutnotifier.services

import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

class RestartWorker(ctx: Context, params: WorkerParameters)
    : Worker(ctx, params) {

    override fun doWork(): Result {
        val intent = Intent(applicationContext, ClockInAndOutForegroundService::class.java)
        applicationContext.startForegroundService(intent)
        return Result.success()
    }

    companion object {
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<RestartWorker>(
                15, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "restart_service",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request
                )
        }
    }
}