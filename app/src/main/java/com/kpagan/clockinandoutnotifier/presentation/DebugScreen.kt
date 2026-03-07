package com.kpagan.clockinandoutnotifier.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DebugScreen(viewModel: SettingsViewModel) {

    val state by viewModel.state.collectAsState()

    Column(Modifier.padding(16.dp)) {

        Text("ClockInAndOut Debug", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        DebugItem("Latitude", state.latitude)
        DebugItem("Longitude", state.longitude)
        DebugItem("Radius", state.radius)
        DebugItem("Silent Mode", state.silentMode.toString())
        DebugItem("Inside URL", state.insideUrl)
        DebugItem("Outside URL", state.outsideUrl)

        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.restartService() }) {
            Text("Restart Monitoring")
        }
    }
}

@Composable
private fun DebugItem(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text("$label: ", fontWeight = FontWeight.Bold)
        Text(value)
    }
}
