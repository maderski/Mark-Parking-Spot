package maderski.markparkingspot;

import android.Manifest;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        setLocationTimeDateText();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MPSNotification notification = new MPSNotification();
        notification.createMessage(this);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_menu) {
            aboutSelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    //Launches the AboutActivity when about is selected
    private void aboutSelected(){
        final View view = findViewById(R.id.toolbar);

        Snackbar.make(view, "Created by: Jason Maderski" + "\n" + "Version: " + showVersion(), Snackbar.LENGTH_LONG).show();
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
        //Get current location and store it in MPSPreferences
        CurrentLocation currentLocation = new CurrentLocation(this);
        MPSPreferences.setLatitude(this, currentLocation.getLatitude());
        MPSPreferences.setLongitude(this, currentLocation.getLongitude());

        //Get current time and date and store it in MPSPreferences
        DateAndTime dateAndTime = new DateAndTime();
        MPSPreferences.setCurrentDate(this, dateAndTime.getCurrentDate());
        MPSPreferences.setCurrentTime(this, dateAndTime.getCurrentTime());

        //Set the UI text to the recently stored values
        setLocationTimeDateText();
    }

    private void setLocationTimeDateText(){
        //Set Lat and Long text
        TextView textView = (TextView)findViewById(R.id.latitudeText);
        textView.setText(MPSPreferences.getLatitude(this));
        textView = (TextView) findViewById(R.id.longitudeText);
        textView.setText(MPSPreferences.getLongitude(this));

        //Set Time and Date text
        textView = (TextView) findViewById(R.id.timeText);
        textView.setText(MPSPreferences.getCurrentTime(this));
        textView = (TextView) findViewById(R.id.dateText);
        textView.setText(MPSPreferences.getCurrentDate(this));
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
}
