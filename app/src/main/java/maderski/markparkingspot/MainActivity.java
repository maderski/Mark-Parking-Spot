package maderski.markparkingspot;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(hasLocationPermission()) {
            Actions actions = new Actions(this);
            actions.getCurrentLocation(60);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_FINE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Actions actions = new Actions(this);
                actions.getCurrentLocation(60);
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
}
