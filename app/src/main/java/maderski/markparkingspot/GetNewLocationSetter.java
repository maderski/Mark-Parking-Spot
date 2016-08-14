package maderski.markparkingspot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by Jason on 8/13/16.
 */
public class GetNewLocationSetter extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean enabled = MPSPreferences.CanGetNewLocation(this);
        if(enabled)
            MPSPreferences.setCanGetNewLocation(this, false);
        else
            MPSPreferences.setCanGetNewLocation(this, true);
        if(BuildConfig.DEBUG)
            Log.i("GNLS:::", Boolean.toString(MPSPreferences.CanGetNewLocation(this)));
        MPSNotification notification = new MPSNotification(this);
        notification.createMessage(true);
        finish();
    }
}
