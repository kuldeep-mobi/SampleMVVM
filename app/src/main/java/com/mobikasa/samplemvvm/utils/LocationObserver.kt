package com.mobikasa.samplemvvm.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.*
import com.vmadalin.easypermissions.EasyPermissions

class LocationObserver(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val callback: (Location?) -> Unit,
) : LifecycleObserver {

    private var enabled = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationSettingRequest: LocationSettingsRequest

    private val locationRequestGPS by lazy {
        LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5 * 1000L)
    }

    private val locationRequestNETWORK by lazy {
        LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_LOW_POWER)
            .setNumUpdates(1)
            .setExpirationDuration(1000)
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            //val data = p0.lastLocation ?: Location("")
            val data = p0?.lastLocation.takeIf { it != null }
            // val cccc = p0?.lastLocation.takeUnless { it == null }
            Log.d("TAG", "Location $data")
            callback(data)
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationSettingRequest =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequestGPS).build()
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (enabled) {
            Log.d("TAG", "Connected")
            fusedLocationProviderClient.requestLocationUpdates(locationRequestGPS, locationCallback,
                Looper.getMainLooper())
        }
    }

    @SuppressLint("MissingPermission")
    fun enable() {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            enabled = true
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                Log.d("TAG", "connect if not connected")
                fusedLocationProviderClient.requestLocationUpdates(locationRequestGPS,
                    locationCallback,
                    Looper.getMainLooper())
            }
        } else {
            EasyPermissions.requestPermissions(context as Activity,
                "Permission Require for location",
                102,
                perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d("TAG", "Disconnect")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}