package maderski.markparkingspot

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class DropPreviousPinActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pinLabel = "Parking Spot " + MPSPreferences.getCurrentDate(this)
        val latitude = MPSPreferences.getLatitude(this)
        val longitude = MPSPreferences.getLongitude(this)

        val dropPinIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=$latitude,$longitude($pinLabel)"))
        startActivity(dropPinIntent)
        finish()
    }
}
