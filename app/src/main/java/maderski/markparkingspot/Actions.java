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
    boolean triedAgain = false;

    public Actions(Context context){
        this.context = context;
        locationManager = new LocationManager(context);
    }

    public void checkIfCanGetLocation(){
        boolean enabled = MPSPreferences.CanGetNewLocation(context);
        boolean gpsIsOn = locationManager.isGPSAndNetworkEnabled();
        if(gpsIsOn) {
            if (enabled)
                getCurrentLocation(15);
            else
                createMessage(true);
        }else{
            Toast.makeText(context, "The GPS is currently disabled!", Toast.LENGTH_LONG).show();
        }
    }

    public void getCurrentLocation(final int seconds){
        final Context ctx = this.context;
        int milliseconds = seconds * 1000;

        locationManager.startLocationListener(context);

        new CountDownTimer(milliseconds, 1000){
            int start = 0;
            int countSeconds = 1;
            String workingMessage = "Working";
            String dot = ".";
            @Override
            public void onTick(long l) {
                if(start < 6 && start > 0) {
                    workingMessage += dot;
                }
                else {
                    workingMessage = "Working";
                    start = 1;
                }

                if(countSeconds < (seconds - 7))
                    Toast.makeText(ctx, workingMessage,Toast.LENGTH_SHORT).show();
                start++;
                countSeconds++;
            }

            @Override
            public void onFinish() {
                locationManager.stopLocationListener(context);
                locationManager.getBestLocation(context);
                performActions();
            }
        }.start();
    }

    private void performActions(){
        if(locationManager.isGPSAndNetworkEnabled()) {
            if(getCurrentLocationTimeDate()){
                createMessage(false);
            }else{
                if(triedAgain){
                Toast.makeText(context, "Unable to get good GPS fix! Please try again",
                        Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "Unable to get good GPS fix! Trying one more time",
                            Toast.LENGTH_LONG).show();
                    triedAgain = true;
                    getCurrentLocation(15);
                }
            }
        }else{
            Toast.makeText(context, "The GPS is currently disabled!", Toast.LENGTH_LONG).show();
        }
    }

    private void createMessage(boolean isAnUpdate){
        MPSNotification notification = new MPSNotification(context);
        notification.createMessage(isAnUpdate, false);
    }

    private boolean getCurrentLocationTimeDate(){
        //Get current location and store it in MPSPreferences
        MPSPreferences.setLatitude(context, locationManager.getLatitude());
        MPSPreferences.setLongitude(context, locationManager.getLongitude());
        MPSPreferences.setAccuracy(context, locationManager.getAccuracyAsString());

        if(locationManager.getAccuracyAsString().equals("unknown")) {
            return false;
        }

        if(locationManager.isAccuracyPoor(locationManager.getLocationResult())){
            return false;
        }

        //Get current time and date and store it in MPSPreferences
        DateAndTime dateAndTime = new DateAndTime();
        MPSPreferences.setCurrentDate(context, dateAndTime.getCurrentDate());
        MPSPreferences.setCurrentTime(context, dateAndTime.getCurrentTime());

        return true;
    }
}
