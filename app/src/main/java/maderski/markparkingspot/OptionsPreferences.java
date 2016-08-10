package maderski.markparkingspot;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jason on 8/9/16.
 */
public class OptionsPreferences {
    public static SharedPreferences.Editor _editor;

    private static String MY_PREFS_NAME = "MPSPreferences";

    private static final String AUTO_CANCEL_KEY = "AutoCancel";
    private static final String ONGOING_KEY = "Ongoing";

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

    public static void setAutoCancel(Context context, boolean enable){
        editor(context).putBoolean(AUTO_CANCEL_KEY, enable);
        commit(context);
    }

    public static boolean getAutoCancel(Context context){
        return reader(context).getBoolean(AUTO_CANCEL_KEY, false);
    }

    public static void setOngoing(Context context, boolean enable){
        editor(context).putBoolean(ONGOING_KEY, enable);
        commit(context);
    }

    public static boolean getOngoing(Context context){
        return reader(context).getBoolean(ONGOING_KEY, false);
    }
}
