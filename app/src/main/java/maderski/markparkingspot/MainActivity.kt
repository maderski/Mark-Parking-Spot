package maderski.markparkingspot

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isLocationPermissionGranted = PermissionUtils.hasLocationPermission(this)
        if (isLocationPermissionGranted) {
            val actions = Actions(this)
            actions.checkIfCanGetLocation()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        finish()
    }
}
