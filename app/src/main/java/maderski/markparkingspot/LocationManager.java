package maderski.markparkingspot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jason on 2/7/16.
 */
public class LocationManager extends LocationUpdater{
    private static final String TAG = LocationManager.class.getName();

    private Location locationResult;

    //Get location
    public LocationManager(Context context){
        super(context);
        checkPermissions(context);
    }

    private void checkPermissions(Context context){
        //Get current GPS and Network permissions
        PackageManager packageManager = context.getPackageManager();
        int hasGPSPermission = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName());
        int hasNetworkPermission = packageManager.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                context.getPackageName());
        //Check if Permission is granted
        if(hasGPSPermission == PackageManager.PERMISSION_GRANTED && hasNetworkPermission == PackageManager.PERMISSION_GRANTED) {
            locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //locationResult = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else {
            if(BuildConfig.DEBUG)
                Log.e(TAG, "Need GPS Permission!!!");
            Toast.makeText(context, "Need GPS Permission!", Toast.LENGTH_LONG).show();
        }
    }

    private void listAllProviders(){
        List<String> matchingProviders = locationManager.getAllProviders();
        for(String provider: matchingProviders){
            Log.i("CurrentLocation", " Provider: " + provider);
            Log.i("CurrentLocation", " Accuracy: " + Float.toString(locationResult.getAccuracy()));
            Log.i("CurrentLocation", " Time: " + Long.toString(locationResult.getTime()));
        }
    }

    public void getBestLocation(Context context){
        if(gpsLock != null){
            if(!isAccuracyPoor(gpsLock)) {
                locationResult = gpsLock;
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "GPS LOCK USED: " + gpsLock.getAccuracy());
                    Toast.makeText(context, "GPS", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        } else if(netLastLock != null) {
            if(!isAccuracyPoor(netLock)) {
                locationResult = netLock;
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "NETWORK LOCK USED: " + netLock.getAccuracy());
                    Toast.makeText(context, "NETWORK", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        } else {
            useLaskKnowLocation(context);
            if(BuildConfig.DEBUG){
                Log.i(TAG, "LAST KNOWN LOCATION USED");
            }
        }
    }

    private void useLaskKnowLocation(Context context){
        getLastKnownLocation(context);
        if(gpsLastLock.getTime() > netLastLock.getTime()) {
            if(gpsLastLock.getTime() > Calendar.getInstance().getTimeInMillis() - 120000) {
                locationResult = gpsLastLock;
                if(BuildConfig.DEBUG) {
                    Toast.makeText(context, "GPS LAST", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            if(netLastLock.getTime() > Calendar.getInstance().getTimeInMillis() - 120000) {
                locationResult = netLastLock;
                if(BuildConfig.DEBUG) {
                    Toast.makeText(context, "NET LAST", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Returns true if GPS is enabled
    public boolean isGPSAndNetworkEnabled(){
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
    }

    //Return Latitude as a String
    public String getLatitude(){
        String latitude = "None";
        try {
            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                if(locationResult != null)
                    latitude = Double.toString(locationResult.getLatitude());
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return latitude;
    }

    //Return Longitude as a String
    public String getLongitude(){
        String longitude = "None";
        try {
            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
                if(locationResult != null)
                    longitude = Double.toString(locationResult.getLongitude());
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return longitude;
    }

    //Return Accuracy as a string
    public String getAccuracyAsString(){
        String Accuracy = "unknown";

        try{
            if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){
                if(locationResult != null) {
                    double locationToFeet = locationResult.getAccuracy() * 3.28;
                    Accuracy = Double.toString(locationToFeet);
                }
            }
        }catch (Exception e){ e.printStackTrace(); }

        return Accuracy;
    }

    public boolean isAccuracyPoor(Location location){
        float Accuracy = 30;
        try{
            if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) &&
                    locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)){
                Accuracy = location.getAccuracy();
            }
        }catch (Exception e){ e.printStackTrace(); }

        if(Accuracy > 16.0){ return true; }
        else{ return false; }
    }

    public Location getLocationResult(){
        return locationResult;
    }
}
