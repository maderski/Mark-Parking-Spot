package maderski.markparkingspot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Jason on 8/9/16.
 */
public class MPSNotification {
    private static final String nTAG = Notification.class.getName();
    private static final int nID = 809;

    private Context context;

    public MPSNotification(Context context){
        this.context = context;
    }

    //Create notification message
    public void createMessage(){
        int color = ContextCompat.getColor(context, R.color.colorAccent);

        String pinLabel = "Parking Spot";
        String latitude = MPSPreferences.getLatitude(context);
        String longitude = MPSPreferences.getLongitude(context);
        String accuracy = MPSPreferences.getAccuracy(context);

        String title = "Captured! w/Accuracy: " + accuracy + "m";
        String message = "at " + MPSPreferences.getCurrentTime(context) +
                " on " + MPSPreferences.getCurrentDate(context);
        String actionText = setActionButtonText();

        PendingIntent pendingIntent = dropPinPendingIntent(latitude, longitude, pinLabel);
        PendingIntent canGetLocationIntent = setCanGetLocationIntent();

        buildNotification(title, message, pendingIntent, canGetLocationIntent, actionText,color);
    }

    //Create pending Intent for notification
    private PendingIntent dropPinPendingIntent(String latitude, String longitude, String pinLabel){
        Intent dropPinIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="+ latitude+","+ longitude+"("+ pinLabel+")"));
        dropPinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, dropPinIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //Create CanGetNewLocation Pending Intent
    private PendingIntent setCanGetLocationIntent(){
        Intent intent = new Intent(context, GetNewLocationSetter.class);

        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //Set Action Button Text
    private String setActionButtonText(){
        boolean enabled = MPSPreferences.CanGetNewLocation(context);

        if(enabled)
            return "Get new location: ON";
        else
            return "Get new location: OFF";

    }

    //Build notification message
    private void buildNotification(String title, String message, PendingIntent pendingIntent,
                                   PendingIntent canGetLocationIntent,String actionText, int color){
        NotificationManager nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_car)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .setColor(color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(android.R.drawable.ic_menu_edit, actionText, canGetLocationIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        nManager.notify(nTAG, nID, builder.build());
    }
}
