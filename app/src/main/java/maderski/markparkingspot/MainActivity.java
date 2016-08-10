package maderski.markparkingspot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        getCurrentLocationTimeDate();

        MPSNotification notification = new MPSNotification();
        notification.createMessage(this);
        finish();

    }

    private void checkLocationPermission() {
        PackageManager packageManager = getPackageManager();
        int hasPermission = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                getPackageName());
        //Check if Permission is granted
        if(hasPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PackageManager.PERMISSION_GRANTED);
        }
    }

    private String showVersion(){
        String version = "none";

        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pkgInfo.versionName;
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

        return version;
    }

    public void getCurrentLocationButton(View view){
        //Gets current Location, Time and Date
        getCurrentLocationTimeDate();

        //Set the UI text to the recently stored values

    }

    public void dropPinInMapsButton(View view){
        String latitude = MPSPreferences.getLatitude(this);
        String longitude = MPSPreferences.getLongitude(this);
        String labelName = "Parking Spot";

        dropPinInMaps(latitude, longitude, labelName);

    }

    private void dropPinInMaps(String latitude, String longitude, String labelName){
        Intent dropPinIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+latitude+","+longitude+"("+labelName+")"));
        startActivity(dropPinIntent);
    }

    private void getCurrentLocationTimeDate(){
        //Get current location and store it in MPSPreferences
        CurrentLocation currentLocation = new CurrentLocation(this);
        MPSPreferences.setLatitude(this, currentLocation.getLatitude());
        MPSPreferences.setLongitude(this, currentLocation.getLongitude());

        //Get current time and date and store it in MPSPreferences
        DateAndTime dateAndTime = new DateAndTime();
        MPSPreferences.setCurrentDate(this, dateAndTime.getCurrentDate());
        MPSPreferences.setCurrentTime(this, dateAndTime.getCurrentTime());
    }
}
