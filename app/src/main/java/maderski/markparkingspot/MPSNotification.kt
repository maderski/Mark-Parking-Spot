package maderski.markparkingspot

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.app.NotificationCompat

/**
 * Created by Jason on 8/9/16.
 */
class MPSNotification(private val context: Context) {

    private val notificationManager: NotificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var title: String? = null
    private var message: String? = null
    private var pendingIntent: PendingIntent? = null
    private var color: Int = 0


    //Create notification message
    fun createMessage(isAnUpdate: Boolean) {
        val enabled = MPSPreferences.canGetNewLocation(context)

        val pinLabel = "Parking Spot " + MPSPreferences.getCurrentDate(context)
        val latitude = MPSPreferences.getLatitude(context)
        val longitude = MPSPreferences.getLongitude(context)
        val accuracy = MPSPreferences.getAccuracy(context)

        title = setTitleText(enabled, isAnUpdate) + accuracy + "ft"
        message = "at " + MPSPreferences.getCurrentTime(context) +
                " on " + MPSPreferences.getCurrentDate(context)
        color = ContextCompat.getColor(context, R.color.colorAccent)
        pendingIntent = dropPinPendingIntent(latitude, longitude, pinLabel)

        buildNotification()
    }

    //Create pending Intent for notification
    private fun dropPinPendingIntent(latitude: String, longitude: String, pinLabel: String): PendingIntent {
        val dropPinIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=$latitude,$longitude($pinLabel)"))
        dropPinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(context, 0, dropPinIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun setTitleText(enabled: Boolean, isAnUpdate: Boolean): String {
        return if (enabled && !isAnUpdate)
            context.getString(R.string.captured_with_accuracy)
        else
            context.getString(R.string.drop_pin_with_accuracy)
    }

    //Build notification message
    private fun buildNotification() {
        val builder = if (Build.VERSION.SDK_INT < 26) {
            NotificationCompat.Builder(context)
        } else {
            val channel = getNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
            notificationManager.createNotificationChannel(channel)
            NotificationCompat.Builder(context, CHANNEL_ID)
        }

        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_car)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setColor(color)
                .priority = NotificationCompat.PRIORITY_DEFAULT

        notificationManager.notify(nTAG, nID, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(channelId: String, channelName: String): NotificationChannel {
        val notificationChannel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        notificationChannel.importance = NotificationManager.IMPORTANCE_DEFAULT
        return notificationChannel
    }

    companion object {
        private const val nTAG = "MPSNotification"
        private const val nID = 809
        private const val CHANNEL_NAME = "Mark Parking Spot"
        private const val CHANNEL_ID = "MPSChannelID"
    }
}
