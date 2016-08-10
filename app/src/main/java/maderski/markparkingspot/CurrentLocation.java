package maderski.markparkingspot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jason on 2/7/16.
 */
public class CurrentLocation {
    private static final String TAG = CurrentLocation.class.getName();

    private LocationManager locationManager;
    private Location location;

    //Get course location
    public CurrentLocation(Context context){

        //Get Permission
        PackageManager packageManager = context.getPackageManager();
        int hasPermission = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName());
        //Check if Permission is granted
        if(hasPermission == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else {
            if(BuildConfig.DEBUG)
                Log.e(TAG, "Need GPS Permission!!!");
            Toast.makeText(context, "Need GPS Permission!", Toast.LENGTH_LONG).show();
        }
    }
    //Return Latitude as a String
    public String getLatitude(){
        String latitude = "28.4158";
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                latitude = Double.toString(location.getLatitude());
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return latitude;
    }

    public String getLatitudeAvg(int numberOfReadings){
        double[] latitudes = new double[numberOfReadings];
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                for (int i = 0; i < numberOfReadings; i++) {
                    latitudes[i] = location.getLatitude();
                    if(BuildConfig.DEBUG)
                        Log.i(TAG, "Lati: " + latitudes[i]);
                }
            }
            else{ return "28.4158"; }
        }catch (Exception e){ Log.e(TAG, e.getMessage()); }

        double sumOflatitudes = 0;
        for(double latitude : latitudes){
            sumOflatitudes += latitude;
        }

        double latitudeAvg = sumOflatitudes/numberOfReadings;
        if(BuildConfig.DEBUG)
            Log.i(TAG, "Latitude Avg: " + latitudeAvg);
        return Double.toString(latitudeAvg).trim();
    }

    //Return Longitude as a String
    public String getLongitude(){
        String longitude = "-81.2989";
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                longitude = Double.toString(location.getLongitude());
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return longitude;
    }

    public String getAccuracy(){
        String Accuracy = "unknown";

        try{
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Accuracy = Float.toString(location.getAccuracy());
            }
        }catch (Exception e){ e.printStackTrace(); }

        return Accuracy;
    }

    public String getLongitudeAvg(int numberOfReadings){
        double[] longitudes = new double[numberOfReadings];
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                for (int i = 0; i < numberOfReadings; i++) {
                    longitudes[i] = location.getLongitude();
                    if(BuildConfig.DEBUG)
                        Log.i(TAG, "Long: " + longitudes[i]);
                }
            }
            else{ return "28.4158"; }
        }catch (Exception e){ Log.e(TAG, e.getMessage()); }

        double sumOflongitudes = 0;
        for(double longitude : longitudes){
            sumOflongitudes += longitude;
        }

        double longitudeAvg = sumOflongitudes/numberOfReadings;
        if(BuildConfig.DEBUG)
            Log.i(TAG, "Longitude Avg: " + longitudeAvg);
        return Double.toString(longitudeAvg).trim();
    }
}
