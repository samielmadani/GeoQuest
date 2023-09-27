package com.example.geoquest.model

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

fun getCurrentLocation(context: Context, onSuccess: (Location) -> Unit, onError: (Exception) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onError(Exception("Permission denied"))
        return
    }

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                onSuccess(location)
            } else {
                onError(Exception("Location is null"))
            }
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}

fun openMap(context: Context, latitude: Double, longitude: Double, label: String) {
    try {
        val uri = Uri.parse("geo:0,0?q=$latitude,$longitude($label)")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No map application found!", Toast.LENGTH_SHORT).show()
    }
}
