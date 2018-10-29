package maderski.markparkingspot

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast


/**
 * Created by Jason on 8/13/16.
 */
class GetNewLocationSetter : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val enabled = MPSPreferences.canGetNewLocation(this)
        if (enabled) {
            MPSPreferences.setCanGetNewLocation(this, false)
            Toast.makeText(this, "Location SAVED!", Toast.LENGTH_SHORT).show()
        } else {
            MPSPreferences.setCanGetNewLocation(this, true)
        }
        if (BuildConfig.DEBUG)
            Log.i("GNLS:::", java.lang.Boolean.toString(MPSPreferences.canGetNewLocation(this)))
        val notification = MPSNotification(this)
        notification.createMessage(true, true)
        finish()
    }
}
