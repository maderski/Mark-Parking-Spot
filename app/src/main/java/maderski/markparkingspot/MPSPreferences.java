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
}