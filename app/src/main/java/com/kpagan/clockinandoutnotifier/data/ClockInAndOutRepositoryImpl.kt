package com.kpagan.clockinandoutnotifier.data

import android.content.Context
import com.kpagan.clockinandoutnotifier.domain.ClockInAndOutConfig
import com.kpagan.clockinandoutnotifier.domain.ClockInAndOutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ClockInAndOutRepositoryImpl(context: Context) : ClockInAndOutRepository {

    private val dataSource = PreferencesDataSource(context)

    override suspend fun saveConfig(config: ClockInAndOutConfig) {
        dataSource.saveConfig(config)
    }

    override suspend fun getConfig(): ClockInAndOutConfig {
        return dataSource.configFlow.first()
    }

    override fun observeConfig(): Flow<ClockInAndOutConfig> {
        return dataSource.configFlow
    }
}