package maderski.markparkingspot;

import android.content.Context;
import android.location.Location;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by Jason on 8/11/16.
 */
public class Actions {

    Context context;
    LocationManager locationManager;
    Location location;

    public Actions(Context context){
        this.context = context;
        locationManager = new LocationManager(context);
    }

    public void checkIfCanGetLocation(){
        boolean enabled = MPSPreferences.CanGetNewLocation(context);
        if(enabled)
            getCurrentLocation(30);
        else
            createMessage();
    }

    public boolean getCurrentLocation(final int seconds){
        final Context ctx = this.context;
        int milliseconds = seconds * 1000;

        locationManager.startLocationListener();

        new CountDownTimer(milliseconds, 1000){
            int start = 1;
            String finish = Integer.toString(seconds) + "sec";
            @Override
            public void onTick(long l) {
                location = locationManager.getLocation();
                if(location == null){
                    Toast.makeText(ctx, "Working..." + Integer.toString(start) + " of " + finish
                            ,Toast.LENGTH_SHORT).show();
                    start++;
                }

                if(location != null) {
                    locationManager.stopLocationListener();
                    performActions();
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                locationManager.stopLocationListener();
            }
        }.start();

        if(location != null)
            return true;
        else
            return false;
    }

    private void performActions(){
        if(locationManager.isGPSEnabled()) {
            if(getCurrentLocationTimeDate()){
                createMessage();
            }else{
                Toast.makeText(context, "Unable to get good GPS fix, Poor Accuracy! Please try again",
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "The GPS is currently disabled!", Toast.LENGTH_LONG).show();
        }
    }

    private void createMessage(){
        MPSNotification notification = new MPSNotification(context);
        notification.createMessage();
    }

    private boolean getCurrentLocationTimeDate(){
        //Get current location and store it in MPSPreferences
        MPSPreferences.setLatitude(context, locationManager.getLatitude());
        MPSPreferences.setLongitude(context, locationManager.getLongitude());
        MPSPreferences.setAccuracy(context, locationManager.getAccuracy());

        if(locationManager.getAccuracy().equals("unknown")) {
            return false;
        }

        if(locationManager.isAccuracyPoor()){
            return false;
        }

        //Get current time and date and store it in MPSPreferences
        DateAndTime dateAndTime = new DateAndTime();
        MPSPreferences.setCurrentDate(context, dateAndTime.getCurrentDate());
        MPSPreferences.setCurrentTime(context, dateAndTime.getCurrentTime());

        return true;
    }
}
