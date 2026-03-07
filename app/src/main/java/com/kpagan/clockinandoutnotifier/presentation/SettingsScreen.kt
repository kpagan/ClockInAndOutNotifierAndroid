package com.kpagan.clockinandoutnotifier.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(viewModel: SettingsViewModel,
                   openMap: () -> Unit,
                   openDebug: () -> Unit) {

    val state by viewModel.state.collectAsState()

    Column(Modifier.padding(16.dp)) {

        Text("Geofence Settings", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.latitude,
            onValueChange = viewModel::onLatitudeChanged,
            label = { Text("Latitude") }
        )

        OutlinedTextField(
            value = state.longitude,
            onValueChange = viewModel::onLongitudeChanged,
            label = { Text("Longitude") }
        )

        OutlinedTextField(
            value = state.radius,
            onValueChange = viewModel::onRadiusChanged,
            label = { Text("Radius (meters)") }
        )

        OutlinedTextField(
            value = state.siteUrl,
            onValueChange = viewModel::onUrlChanged,
            label = { Text("Url to open") }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Silent mode")
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = state.silentMode,
                onCheckedChange = viewModel::onSilentChanged
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = openMap) {
            Text("Pick Location on Map")
        }

        Button(onClick = openDebug) {
            Text("Open Debug Screen")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = { viewModel.save() }) {
            Text("Save & Activate")
        }
    }
}