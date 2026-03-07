package com.kpagan.clockinandoutnotifier.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPickerScreen(viewModel: SettingsViewModel) {

    val state by viewModel.state.collectAsState()

    val selected = LatLng(
        state.latitude.toDoubleOrNull() ?: 0.0,
        state.longitude.toDoubleOrNull() ?: 0.0
    )

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selected, 15f)
    }

    Column {

        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraState,
            onMapClick = { latLng ->
                viewModel.setLocation(latLng.latitude, latLng.longitude)
            }
        ) {
            Marker(state = MarkerState(position = selected))
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { viewModel.save() }
        ) {
            Text("Use This Location")
        }
    }
}
