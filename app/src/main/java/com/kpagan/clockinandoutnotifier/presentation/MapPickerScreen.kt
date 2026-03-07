package com.kpagan.clockinandoutnotifier.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPickerScreen(viewModel: SettingsViewModel) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val lat = state.latitude.toDoubleOrNull() ?: 0.0
    val lng = state.longitude.toDoubleOrNull() ?: 0.0
    val hasLocation = lat != 0.0 || lng != 0.0
    val selected = LatLng(lat, lng)

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selected, 15f)
    }

    val hasPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // If no initial location, try to center on user's current location
    if (!hasLocation && hasPermission) {
        val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
        LaunchedEffect(Unit) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    cameraState.position = CameraPosition.fromLatLngZoom(
                        LatLng(it.latitude, it.longitude), 15f
                    )
                }
            }
        }
    }

    Column {

        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraState,
            properties = MapProperties(isMyLocationEnabled = hasPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = hasPermission),
            onMapClick = { latLng ->
                viewModel.setLocation(latLng.latitude, latLng.longitude)
            }
        ) {
            if (hasLocation) {
                Marker(state = MarkerState(position = selected))
            }
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
