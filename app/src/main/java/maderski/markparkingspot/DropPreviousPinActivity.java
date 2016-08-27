package maderski.markparkingspot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DropPreviousPinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String pinLabel = "Parking Spot " + MPSPreferences.getCurrentDate(this);
        String latitude = MPSPreferences.getLatitude(this);
        String longitude = MPSPreferences.getLongitude(this);

        Intent dropPinIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="+ latitude+","+ longitude+"("+ pinLabel+")"));
        startActivity(dropPinIntent);
        finish();
    }
}
