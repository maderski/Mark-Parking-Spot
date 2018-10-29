package maderski.markparkingspot

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

object PermissionUtils {
    fun hasLocationPermission(activity: Activity): Boolean {
        val packageManager = activity.packageManager
        val hasGPSPermission = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                activity.packageName)
        val hasNetworkPermission = packageManager.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                activity.packageName)
        //Check if Permission is granted
        return if (hasGPSPermission != PackageManager.PERMISSION_GRANTED && hasNetworkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PackageManager.PERMISSION_GRANTED)
            false
        } else {
            true
        }
    }
}