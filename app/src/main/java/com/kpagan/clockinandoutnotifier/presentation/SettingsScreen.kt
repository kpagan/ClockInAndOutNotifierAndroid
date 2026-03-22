package com.kpagan.clockinandoutnotifier.presentation

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun SettingsScreen(viewModel: SettingsViewModel,
                   openMap: () -> Unit) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(Modifier.padding(16.dp)) {

        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.latitude,
            onValueChange = viewModel::onLatitudeChanged,
            label = { Text("Latitude") },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = state.longitude,
            onValueChange = viewModel::onLongitudeChanged,
            label = { Text("Longitude") },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = state.radius,
            onValueChange = viewModel::onRadiusChanged,
            label = { Text("Radius (meters)") },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = state.siteUrl,
            onValueChange = viewModel::onUrlChanged,
            label = { Text("Url to open") },
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
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

        Button(onClick = {
            if (state.siteUrl.isNotBlank()) {
                val url = if (!state.siteUrl.startsWith("http://") && !state.siteUrl.startsWith("https://")) {
                    "https://${state.siteUrl}"
                } else {
                    state.siteUrl
                }
                try {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Handle potential parsing errors or no browser available
                }
            }
        }) {
            Text("Open Site")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = { viewModel.save() }) {
            Text("Save & Activate")
        }
    }
}