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

    private NotificationManager nManager;
    private NotificationCompat.Builder builder;

    private String title;
    private String message;
    private PendingIntent pendingIntent;
    private PendingIntent canGetLocationIntent;
    private String actionText;

    private int color;
    private int actionIcon;


    public MPSNotification(Context context){
        this.context = context;
        this.nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        this.builder = new NotificationCompat.Builder(context);
    }

    //Create notification message
    public void createMessage(boolean isAnUpdate, boolean isAButtonPress){
        boolean enabled = MPSPreferences.CanGetNewLocation(context);

        String pinLabel = "Parking Spot " + MPSPreferences.getCurrentDate(context);
        String latitude = MPSPreferences.getLatitude(context);
        String longitude = MPSPreferences.getLongitude(context);
        String accuracy = MPSPreferences.getAccuracy(context);

        title = setTitleText(enabled, isAnUpdate) + accuracy + "ft";
        message = "at " + MPSPreferences.getCurrentTime(context) +
                " on " + MPSPreferences.getCurrentDate(context);
        actionText = setActionButtonText(enabled);

        color = ContextCompat.getColor(context, R.color.colorAccent);
        actionIcon = setActionButtonIcon(enabled);

        pendingIntent = dropPinPendingIntent(latitude, longitude, pinLabel);
        canGetLocationIntent = setCanGetLocationIntent();

        if(isAButtonPress)
            buildNoVibrateNotification();
        else
            buildNotification();
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
    private String setActionButtonText(boolean enabled){
        if(enabled)
            return "Get new location: ON";
        else
            return "Get new location: OFF";

    }

    private int setActionButtonIcon(boolean enabled){
        if(enabled)
            return R.drawable.ic_map_pin;
        else
            return android.R.drawable.ic_menu_close_clear_cancel;
    }

    private String setTitleText(boolean enabled, boolean isAnUpdate){
        if(enabled && !isAnUpdate)
            return "Captured! w/Accuracy: ";
        else
            return "Drop pin w/Accuracy: ";
    }

    //Build notification message
    private void buildNotification(){
        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_car)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .setColor(color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(actionIcon, actionText, canGetLocationIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        nManager.notify(nTAG, nID, builder.build());
    }

    private void buildNoVibrateNotification(){
        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_car)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .setColor(color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(actionIcon, actionText, canGetLocationIntent);
        nManager.notify(nTAG, nID, builder.build());
    }
}
