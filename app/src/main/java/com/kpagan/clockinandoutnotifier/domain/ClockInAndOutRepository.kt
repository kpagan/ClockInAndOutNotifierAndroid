package com.kpagan.clockinandoutnotifier.domain

import kotlinx.coroutines.flow.Flow

interface ClockInAndOutRepository {

    suspend fun saveConfig(config: ClockInAndOutConfig)

    suspend fun getConfig(): ClockInAndOutConfig

    fun observeConfig(): Flow<ClockInAndOutConfig>
}