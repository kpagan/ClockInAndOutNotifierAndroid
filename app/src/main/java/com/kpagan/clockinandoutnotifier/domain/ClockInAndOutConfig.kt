package com.kpagan.clockinandoutnotifier.domain

data class ClockInAndOutConfig(val latitude: Double,
                               val longitude: Double,
                               val radius: Float,
                               val siteUrl: String,
                               val silentMode: Boolean) {
}