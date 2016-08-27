package maderski.markparkingspot;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DropPreviousPinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MPSNotification notification = new MPSNotification(this);
        notification.createMessage(true, false);
        finish();
    }
}
