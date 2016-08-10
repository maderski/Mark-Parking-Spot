package maderski.markparkingspot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Jason on 8/9/16.
 */
public class MPSNotification {
    private static final String nTAG = Notification.class.getName();
    private static final int nID = 809;

    //Create notification message for BAPM
    public void createMessage(Context context){
        int color = ContextCompat.getColor(context, R.color.colorAccent);

        String pinLabel = "Parking Spot";
        String latitude = MPSPreferences.getLatitude(context);
        String longitude = MPSPreferences.getLongitude(context);
        String accuracy = MPSPreferences.getAccuracy(context);

        String title = "Captured! w/Accuracy: " + accuracy + "m";
        String message = "at " + MPSPreferences.getCurrentTime(context) +
                " on " + MPSPreferences.getCurrentDate(context);

//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                new Intent(context, Options.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Intent dropPinIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="+ latitude+","+ longitude+"("+ pinLabel+")"));
        dropPinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent mapIntent = PendingIntent.getActivity(context, 0, dropPinIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(mapIntent)
                .setColor(color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE);
//                .addAction(android.R.drawable.ic_menu_edit, "Options", contentIntent);
        nManager.notify(nTAG, nID, builder.build());
    }

    //Remove notification that was created
    public void removeMessage(Context context){
        NotificationManager nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            nManager.cancel(nTAG, nID);
        }catch(Exception e){
            Log.e(nTAG, e.getMessage());
        }
    }

    private String shortenString(String inputString, int maxLength){
        if(inputString.length() > maxLength)
            return inputString.substring(0, maxLength);
        else
            return inputString;
    }
}
