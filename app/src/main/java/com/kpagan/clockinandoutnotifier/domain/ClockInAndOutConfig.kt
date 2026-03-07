package com.kpagan.clockinandoutnotifier.domain

data class ClockInAndOutConfig(val latitude: Double,
                               val longitude: Double,
                               val radius: Float,
                               val insideUrl: String,
                               val outsideUrl: String,
                               val silentMode: Boolean) {
}