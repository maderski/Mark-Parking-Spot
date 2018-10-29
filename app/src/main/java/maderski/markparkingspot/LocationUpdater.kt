package maderski.markparkingspot

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat

/**
 * Created by Jason on 8/11/16.
 */
abstract class LocationUpdater(context: Context) {
    var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var netLock: Location? = null
    var netLastLock: Location? = null
    var gpsLock: Location? = null
    var gpsLastLock: Location? = null

    private val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //Called when a new location is found by the GPS location provider
            gpsLock = location
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}

        override fun onProviderEnabled(s: String) {}

        override fun onProviderDisabled(s: String) {}
    }

    private val networkLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //Called when a new location is found by the network location provider
            netLock = location
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}

        override fun onProviderEnabled(s: String) {}

        override fun onProviderDisabled(s: String) {}
    }

    //Register the listener with the Location Manager to receive location updates
    fun startLocationListener(context: Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, gpsLocationListener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, networkLocationListener)
    }

    fun stopLocationListener(context: Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager.removeUpdates(gpsLocationListener)
        locationManager.removeUpdates(networkLocationListener)
    }

    fun getLastKnownLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        gpsLastLock = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        netLastLock = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }
}
