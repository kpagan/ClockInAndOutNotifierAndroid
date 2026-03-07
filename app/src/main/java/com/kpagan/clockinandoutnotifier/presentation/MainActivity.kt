package com.kpagan.clockinandoutnotifier.presentation

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.kpagan.clockinandoutnotifier.services.ClockInAndOutBroadcastReceiver
import com.kpagan.clockinandoutnotifier.ui.theme.ClockInAndOutNotifierTheme

class MainActivity : AppCompatActivity() {

    private lateinit var geofencingClient: GeofencingClient

    private val latitude = 37.9838      // CHANGE THIS
    private val longitude = 23.7275     // CHANGE THIS
    private val radius = 150f           // meters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ClockInAndOutNotifierTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val viewModel: SettingsViewModel = viewModel()

                    NavHost(navController, startDestination = "settings") {

                        composable("settings") {
                            SettingsScreen(
                                viewModel = viewModel,
                                openMap = { navController.navigate("map") },
                                openDebug = { navController.navigate("debug") }
                            )
                        }

                        composable("map") {
                            MapPickerScreen(viewModel)
                        }

                        composable("debug") {
                            DebugScreen(viewModel)
                        }
                    }
                }
            }
        }

        geofencingClient = LocationServices.getGeofencingClient(this)
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1001)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty()) {
            addGeofence()
        }
    }

    private fun addGeofence() {

        val geofence = Geofence.Builder()
            .setRequestId("TARGET_AREA")
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER or
                        Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(this, ClockInAndOutBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
    }
}
