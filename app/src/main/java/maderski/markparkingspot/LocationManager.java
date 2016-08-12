package maderski.markparkingspot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jason on 2/7/16.
 */
public class LocationManager extends LocationUpdater{
    private static final String TAG = LocationManager.class.getName();

    //Get course location
    public LocationManager(Context context){
        super(context);
        //Get Permission
        PackageManager packageManager = context.getPackageManager();
        int hasPermission = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName());
        //Check if Permission is granted
        if(hasPermission == PackageManager.PERMISSION_GRANTED) {
            locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
            Log.i("CurrentLocation", " Accuracy: " + Float.toString(location.getAccuracy()));
            Log.i("CurrentLocation", " Time: " + Long.toString(location.getTime()));
        }
    }

    //Returns true if GPS is enabled
    public boolean isGPSEnabled(){
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    //Return Latitude as a String
    public String getLatitude(){
        String latitude = "None";
        try {
            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                if(location != null)
                    latitude = Double.toString(location.getLatitude());
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
                if(location != null)
                    longitude = Double.toString(location.getLongitude());
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return longitude;
    }

    //Return Accuracy as a string
    public String getAccuracy(){
        String Accuracy = "unknown";

        try{
            if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){
                if(location != null)
                    Accuracy = Float.toString(location.getAccuracy());
            }
        }catch (Exception e){ e.printStackTrace(); }

        return Accuracy;
    }

    public boolean isAccuracyPoor(){
        float Accuracy = 30;
        try{
            if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){
                Accuracy = location.getAccuracy();
            }
        }catch (Exception e){ e.printStackTrace(); }

        if(Accuracy > 20.0){ return true; }
        else{ return false; }
    }
}
