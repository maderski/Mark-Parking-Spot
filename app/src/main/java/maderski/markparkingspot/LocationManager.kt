package maderski.markparkingspot

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import android.location.LocationManager

import java.util.Calendar

/**
 * Created by Jason on 2/7/16.
 */
class LocationManager(context: Context) : LocationUpdater(context) {

    var locationResult: Location? = null
        private set

    //Returns true if GPS is enabled
    val isGPSAndNetworkEnabled: Boolean
        get() = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)

    //Return Latitude as a String
    val latitude: String
        get() {
            var latitude = "None"
            try {
                if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    locationResult?.let {
                        latitude = java.lang.Double.toString(it.latitude)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }

            return latitude
        }

    //Return Longitude as a String
    val longitude: String
        get() {
            var longitude = "None"
            try {
                val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (isGPSEnabled) {
                    locationResult?.let {
                        longitude = it.longitude.toString()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }

            return longitude
        }

    //Return Accuracy as a string
    val accuracyAsString: String
        get() {
            return try {
                val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (isGPSEnabled) {
                    var accuracy = "unknown"
                    locationResult?.let {
                        val locationToFeet = it.accuracy * 3.28
                        accuracy = locationToFeet.toString()
                    }
                    accuracy
                } else {
                    "unknown"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "unknown"
            }
        }

    init {
        checkPermissions(context)
    }

    private fun checkPermissions(context: Context) {
        //Get current GPS and Network permissions
        val packageManager = context.packageManager
        val hasGPSPermission = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                context.packageName)
        val hasNetworkPermission = packageManager.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                context.packageName)
        //Check if Permission is granted
        if (hasGPSPermission == PackageManager.PERMISSION_GRANTED && hasNetworkPermission == PackageManager.PERMISSION_GRANTED) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        } else {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "Need GPS Permission!!!")
            Toast.makeText(context, "Need GPS Permission!", Toast.LENGTH_LONG).show()
        }
    }

    private fun listAllProviders() {
        val matchingProviders = locationManager.allProviders
        for (provider in matchingProviders) {
            locationResult?.let {
                Log.i("CurrentLocation", " Provider: $provider")
                Log.i("CurrentLocation", " Accuracy: " + java.lang.Float.toString(it.accuracy))
                Log.i("CurrentLocation", " Time: " + java.lang.Long.toString(it.time))
            }
        }
    }

    fun getBestLocation(context: Context) {
        if (gpsLock != null) {
            val isAccuracyGood = isAccuracyPoor(gpsLock as Location).not()
            if (isAccuracyGood) {
                locationResult = gpsLock
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "GPS LOCK USED: " + gpsLock?.accuracy)
                    Toast.makeText(context, "GPS", Toast.LENGTH_SHORT).show()
                }
                return
            }
        } else if (netLastLock != null && netLock != null) {
            val isAccuracyGood = isAccuracyPoor(netLock as Location).not()
            if (isAccuracyGood) {
                locationResult = netLock
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "NETWORK LOCK USED: " + netLock?.accuracy)
                    Toast.makeText(context, "NETWORK", Toast.LENGTH_SHORT).show()
                }
                return
            }
        } else {
            useLaskKnowLocation(context)
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "LAST KNOWN LOCATION USED")
            }
        }
    }

    private fun useLaskKnowLocation(context: Context) {
        getLastKnownLocation(context)
        if (gpsLastLock != null && gpsLastLock != null) {
            val gpsLastLockTime = (gpsLastLock as Location).time
            val netLastLockTime = (netLastLock as Location).time
            if (gpsLastLockTime > netLastLockTime) {
                if (gpsLastLockTime > Calendar.getInstance().timeInMillis - 120000) {
                    locationResult = gpsLastLock
                }
            } else {
                if (netLastLockTime > Calendar.getInstance().timeInMillis - 120000) {
                    locationResult = netLastLock
                }
            }
        }
    }

    fun isAccuracyPoor(location: Location): Boolean {
        var accuracy = 31f
        try {
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnabled && isNetworkEnabled) {
                accuracy = location.accuracy
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return accuracy > 30.0f
    }

    companion object {
        private const val TAG = "LocationManager"
    }
}
