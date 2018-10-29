package maderski.markparkingspot

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Jason on 8/9/16.
 */
object MPSPreferences {
    private const val MY_PREFS_NAME = "MPSPreferences"

    private const val LATITUDE_KEY = "MyLatitudeKey"
    private const val LONGITUDE_KEY = "LongitudeKey"
    private const val ACCURACY_KEY = "AccuracyKey"
    private const val CURRENT_TIME_KEY = "CurrentTimeKey"
    private const val CURRENT_DATE_KEY = "CurrentDateKey"
    private const val CAN_GET_NEW_LOCATION = "canGetNewLocation"

    fun setCanGetNewLocation(context: Context, enabled: Boolean) = editor(context).putBoolean(CAN_GET_NEW_LOCATION, enabled).apply()
    fun canGetNewLocation(context: Context): Boolean = reader(context).getBoolean(CAN_GET_NEW_LOCATION, true)

    fun setLatitude(context: Context, latitude: String) = editor(context).putString(LATITUDE_KEY, latitude).apply()
    fun getLatitude(context: Context): String = reader(context).getString(LATITUDE_KEY, "28.4158") ?: "28.4158"

    fun setLongitude(context: Context, longitude: String) = editor(context).putString(LONGITUDE_KEY, longitude).apply()
    fun getLongitude(context: Context): String = reader(context).getString(LONGITUDE_KEY, "-81.2989") ?: "-81.2989"

    fun setAccuracy(context: Context, accuracy: String) = editor(context).putString(ACCURACY_KEY, accuracy).apply()
    fun getAccuracy(context: Context): String = reader(context).getString(ACCURACY_KEY, "Unknown") ?: "Unknown"

    fun setCurrentTime(context: Context, time: String) = editor(context).putString(CURRENT_TIME_KEY, time).apply()
    fun getCurrentTime(context: Context): String = reader(context).getString(CURRENT_TIME_KEY, "0:00 AM") ?: "0:00 AM"

    fun setCurrentDate(context: Context, date: String) = editor(context).putString(CURRENT_DATE_KEY, date).apply()
    fun getCurrentDate(context: Context): String = reader(context).getString(CURRENT_DATE_KEY, "1-1-2000") ?: "1-1-2000"

    //Writes to SharedPreferences, but still need to commit setting to save it
    private fun editor(context: Context): SharedPreferences.Editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()

    //Reads SharedPreferences value
    private fun reader(context: Context): SharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
}
