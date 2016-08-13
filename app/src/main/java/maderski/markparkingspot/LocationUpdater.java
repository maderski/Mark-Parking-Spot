package maderski.markparkingspot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Jason on 8/11/16.
 */
public abstract class LocationUpdater {
    LocationManager locationManager;
    Location location;
    Context context;
    boolean isCurrentLocation = false;

    public LocationUpdater(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Called when a new location is found by the network location provider
            setCurrentLocation(setLocation(location));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private boolean isCurrentLocation() {
        return isCurrentLocation;
    }

    private void setCurrentLocation(boolean currentLocation) {
        isCurrentLocation = currentLocation;
    }

    private boolean setLocation(Location location) {
        this.location = location;
        return true;
    }

    public Location getLocation() {
        if (isCurrentLocation()) {
            setCurrentLocation(false);
            return this.location;
        } else {
            return null;
        }
    }

    //Register the listener with the Location Manager to receive location updates
    public void startLocationListener() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void stopLocationListener(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        locationManager.removeUpdates(locationListener);
    }
}
