package maderski.markparkingspot

import android.content.Context
import android.location.Location
import android.os.CountDownTimer
import android.widget.Toast

/**
 * Created by Jason on 8/11/16.
 */
class Actions(private val context: Context) {
    private val locationManager: LocationManager = LocationManager(context)

    private var triedAgain = false
    private var toast: Toast? = null

    /**
    Get current location and store it in MPSPreferences
    Get current time and date and store it in MPSPreferences
     */
    private val currentLocationTimeDate: Boolean
        get() {
            MPSPreferences.setLatitude(context, locationManager.latitude)
            MPSPreferences.setLongitude(context, locationManager.longitude)
            MPSPreferences.setAccuracy(context, locationManager.accuracyAsString)

            if (locationManager.accuracyAsString == "unknown") {
                return false
            }

            locationManager.locationResult?.let {
                if (locationManager.isAccuracyPoor(it)) {
                    return false
                }
            }

            val dateAndTime = DateAndTime()
            MPSPreferences.setCurrentDate(context, dateAndTime.currentDate)
            MPSPreferences.setCurrentTime(context, dateAndTime.currentTime)

            return true
        }

    fun checkIfCanGetLocation() {
        val enabled = MPSPreferences.canGetNewLocation(context)
        val gpsIsOn = locationManager.isGPSAndNetworkEnabled
        if (gpsIsOn) {
            if (enabled)
                getCurrentLocation(15)
            else
                createMessage(true)
        } else {
            Toast.makeText(context, "The GPS is currently disabled!", Toast.LENGTH_LONG).show()
        }
    }

    fun getCurrentLocation(seconds: Int) {
        val ctx = this.context
        val milliseconds = seconds * 1000

        locationManager.startLocationListener(context)

        object : CountDownTimer(milliseconds.toLong(), 1000) {
            var start = 0
            var countSeconds = 1
            var workingMessage = "Working"
            var dot = "."
            override fun onTick(l: Long) {
                if (start in 1..5) {
                    workingMessage += dot
                } else {
                    workingMessage = "Working"
                    start = 1
                }

                if (countSeconds < seconds - 7) {
                    toast?.let {
                        if (it.view.isShown) {
                            it.cancel()
                            toast = null
                        }
                    }
                    toast = Toast.makeText(ctx, workingMessage, Toast.LENGTH_SHORT)
                    toast?.show()
                }
                start++
                countSeconds++
            }

            override fun onFinish() {
                locationManager.stopLocationListener(context)
                locationManager.getBestLocation(context)
                performActions()
            }
        }.start()
    }

    private fun performActions() {
        if (locationManager.isGPSAndNetworkEnabled) {
            if (currentLocationTimeDate) {
                createMessage(false)
            } else {
                if (triedAgain) {
                    Toast.makeText(context, "Unable to get good GPS fix! Please try again",
                            Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Unable to get good GPS fix! Trying one more time",
                            Toast.LENGTH_LONG).show()
                    triedAgain = true
                    getCurrentLocation(15)
                }
            }
        } else {
            Toast.makeText(context, "The GPS is currently disabled!", Toast.LENGTH_LONG).show()
        }
    }

    private fun createMessage(isAnUpdate: Boolean) {
        val notification = MPSNotification(context)
        notification.createMessage(isAnUpdate, false)
    }
}
