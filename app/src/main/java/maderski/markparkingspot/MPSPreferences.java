package maderski.markparkingspot;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jason on 8/9/16.
 */
public class MPSPreferences {
    public static SharedPreferences.Editor _editor;

    private static String MY_PREFS_NAME = "MPSPreferences";

    private static final String LATITUDE_KEY = "MyLatitudeKey";
    private static final String LONGITUDE_KEY = "LongitudeKey";
    private static final String ACCURACY_KEY = "AccuracyKey";
    private static final String CURRENT_TIME_KEY = "CurrentTimeKey";
    private static final String CURRENT_DATE_KEY = "CurrentDateKey";
    private static final String CAN_GET_NEW_LOCATION = "CanGetNewLocation";

    //Writes to SharedPreferences, but still need to commit setting to save it
    private static SharedPreferences.Editor editor(Context context){

        if(_editor == null){
            _editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
            _editor.commit();
        }

        return _editor;
    }

    //Reads SharedPreferences value
    private static SharedPreferences reader(Context context){

        return context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    //Commits write to SharedPreferences
    private static void commit(Context context){
        editor(context).commit();
        _editor = null;
    }

    public static void setCanGetNewLocation(Context context, boolean enabled){
        editor(context).putBoolean(CAN_GET_NEW_LOCATION, enabled);
        commit(context);
    }

    public static boolean CanGetNewLocation(Context context){
        return reader(context).getBoolean(CAN_GET_NEW_LOCATION, true);
    }

    public static void setLatitude(Context context, String latitude){
        editor(context).putString(LATITUDE_KEY, latitude);
        commit(context);
    }

    public static String getLatitude(Context context){
        return reader(context).getString(LATITUDE_KEY, "28.4158");
    }

    public static void setLongitude(Context context, String longitude){
        editor(context).putString(LONGITUDE_KEY, longitude);
        commit(context);
    }

    public static String getLongitude(Context context){
        return reader(context).getString(LONGITUDE_KEY, "-81.2989");
    }

    public static void setAccuracy(Context context, String accuracy){
        editor(context).putString(ACCURACY_KEY, accuracy);
        commit(context);
    }

    public static String getAccuracy(Context context){
        return reader(context).getString(ACCURACY_KEY, "Unknown");
    }

    public static void setCurrentTime(Context context, String time){
        editor(context).putString(CURRENT_TIME_KEY, time);
        commit(context);
    }

    public static String getCurrentTime(Context context){
        return reader(context).getString(CURRENT_TIME_KEY, "0:00 AM");
    }

    public static void setCurrentDate(Context context, String date){
        editor(context).putString(CURRENT_DATE_KEY, date);
        commit(context);
    }

    public static String getCurrentDate(Context context){
        return reader(context).getString(CURRENT_DATE_KEY, "1-1-2000");
    }
}
