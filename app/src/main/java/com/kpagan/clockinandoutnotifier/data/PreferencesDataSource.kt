package com.kpagan.clockinandoutnotifier.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kpagan.clockinandoutnotifier.domain.ClockInAndOutConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesDataSource(private val context: Context) {

    private object Keys {
        val LAT = doublePreferencesKey("lat")
        val LNG = doublePreferencesKey("lng")
        val RADIUS = floatPreferencesKey("radius")
        val INSIDE = stringPreferencesKey("inside")
        val OUTSIDE = stringPreferencesKey("outside")
        val SILENT = booleanPreferencesKey("silent")
    }

    suspend fun saveConfig(config: ClockInAndOutConfig) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LAT] = config.latitude
            prefs[Keys.LNG] = config.longitude
            prefs[Keys.RADIUS] = config.radius
            prefs[Keys.INSIDE] = config.insideUrl
            prefs[Keys.OUTSIDE] = config.outsideUrl
            prefs[Keys.SILENT] = config.silentMode
        }
    }

    val configFlow: Flow<ClockInAndOutConfig> = context.dataStore.data.map { prefs ->
        ClockInAndOutConfig(
            latitude = prefs[Keys.LAT] ?: 0.0,
            longitude = prefs[Keys.LNG] ?: 0.0,
            radius = prefs[Keys.RADIUS] ?: 150f,
            insideUrl = prefs[Keys.INSIDE] ?: "",
            outsideUrl = prefs[Keys.OUTSIDE] ?: "",
            silentMode = prefs[Keys.SILENT] ?: false
        )
    }
}
