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
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                return Double.toString(location.getLatitude());
            else
                return "28.4158";
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return "28.4158";
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
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                return Double.toString(location.getLongitude());
            else
                return "-81.2989";
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return "-81.2989";
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
