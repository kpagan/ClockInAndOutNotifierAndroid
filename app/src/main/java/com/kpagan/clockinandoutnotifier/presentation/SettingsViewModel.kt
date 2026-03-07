package com.kpagan.clockinandoutnotifier.presentation

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kpagan.clockinandoutnotifier.data.ClockInAndOutRepositoryImpl
import com.kpagan.clockinandoutnotifier.domain.ClockInAndOutConfig
import com.kpagan.clockinandoutnotifier.services.ClockInAndOutForegroundService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SettingsState(
    val latitude: String = "",
    val longitude: String = "",
    val radius: String = "150",
    val siteUrl: String = "",
    val silentMode: Boolean = false
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ClockInAndOutRepositoryImpl(application)

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    init {
        viewModelScope.launch {
            val config = repo.getConfig()
            _state.value = SettingsState(
                latitude = config.latitude.toString(),
                longitude = config.longitude.toString(),
                radius = config.radius.toString(),
                siteUrl = config.siteUrl,
                silentMode = config.silentMode
            )
        }
    }

    fun setLocation(lat: Double, lng: Double) {
        _state.value = _state.value.copy(
            latitude = lat.toString(),
            longitude = lng.toString()
        )
    }

    fun onLatitudeChanged(v: String) { _state.value = _state.value.copy(latitude = v) }
    fun onLongitudeChanged(v: String) { _state.value = _state.value.copy(longitude = v) }
    fun onUrlChanged(v: String) { _state.value = _state.value.copy(siteUrl = v) }
    fun onRadiusChanged(v: String) { _state.value = _state.value.copy(radius = v) }
    fun onSilentChanged(v: Boolean) { _state.value = _state.value.copy(silentMode = v) }

    fun save() {
        viewModelScope.launch {

            val config = ClockInAndOutConfig(
                latitude = _state.value.latitude.toDouble(),
                longitude = _state.value.longitude.toDouble(),
                radius = _state.value.radius.toFloat(),
                siteUrl = _state.value.siteUrl,
                silentMode = _state.value.silentMode
            )

            repo.saveConfig(config)
            restartService()
        }
    }

    fun restartService() {
        val ctx = getApplication<Application>()
        val intent = Intent(ctx, ClockInAndOutForegroundService::class.java)
        ctx.startForegroundService(intent)
    }
}