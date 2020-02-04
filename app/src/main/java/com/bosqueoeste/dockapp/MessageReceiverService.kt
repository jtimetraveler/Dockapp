package com.bosqueoeste.dockapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageReceiverService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title
        val message = remoteMessage.notification?.body
        showNotification(title = title, message = message)
    }

    private fun showNotification(title: String?, message: String?) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, ID_CHANNEL)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setContentIntent(pendingIntent)
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }

        val managerNotification =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        managerNotification.notify(ID_NOTIFICATION, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels() {

        // create android channel
        val androidChannel = NotificationChannel(
            ID_CHANNEL,
            NAME_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT
        )
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true)
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true)
        // Sets the notification light color for notifications posted to this channel
        androidChannel.lightColor = getColor(android.R.color.holo_green_dark)
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            androidChannel
        )

    }

    companion object {

        private const val REQUEST_CODE = 1
        private const val ID_NOTIFICATION = 4321

        const val ID_CHANNEL = "dockapp.NOTITIFICATION_DEFAULT"
        const val NAME_CHANNEL = "NOTIFICATION CHANNEL"
    }
}