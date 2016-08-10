package maderski.markparkingspot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(hasLocationPermission()) {
            performActions();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    private void performActions(){
        CurrentLocation currentLocation = new CurrentLocation(this);
        if(currentLocation.isGPSEnabled()) {
            if(getCurrentLocationTimeDate(currentLocation)){
                createMessage();
            }else{
                Toast.makeText(this, "Unable to get GPS fix, Please try again", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "The GPS is currently disabled!", Toast.LENGTH_LONG).show();
        }
    }

    private void createMessage(){
        MPSNotification notification = new MPSNotification();
        notification.createMessage(this);
    }

    private boolean getCurrentLocationTimeDate(CurrentLocation currentLocation){
        //Get current location and store it in MPSPreferences
        MPSPreferences.setLatitude(this, currentLocation.getLatitude());
        MPSPreferences.setLongitude(this, currentLocation.getLongitude());
        MPSPreferences.setAccuracy(this, currentLocation.getAccuracy());

        if(currentLocation.getAccuracy().equals("unknown")) {
            return false;
        }

        //Get current time and date and store it in MPSPreferences
        DateAndTime dateAndTime = new DateAndTime();
        MPSPreferences.setCurrentDate(this, dateAndTime.getCurrentDate());
        MPSPreferences.setCurrentTime(this, dateAndTime.getCurrentTime());

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_FINE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                performActions();
            }
        }
    }

    private boolean hasLocationPermission() {
        PackageManager packageManager = getPackageManager();
        int hasPermission = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                getPackageName());
        //Check if Permission is granted
        if(hasPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                    //PackageManager.PERMISSION_GRANTED);
            return false;
        }else{
            return true;
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

    private void dropPinInMaps(String latitude, String longitude, String labelName){
        Intent dropPinIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+latitude+","+longitude+"("+labelName+")"));
        startActivity(dropPinIntent);
    }
}
