package com.example.authandmessaging

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseInstanceIDService: FirebaseMessagingService() {

    override fun onNewToken(token: String){
        super.onNewToken(token)

        Log.d("REFRESH TOKEN", token)
    }

    override  fun onMessageReceived(message: RemoteMessage){
        super.onMessageReceived(message)

        Log.d("MESSAGE", "From: ${message.from}")

        //check if messaege is not empty
        message.data.isNotEmpty().let {

            Log.d("MESSAGE PAYLOAD", message.data.toString())

        }

            message.notification?.body?.let{
                msg -> sendMyNotification(msg)
        }
    }

    private fun sendMyNotification (message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)

        val defaultSoundUrl = RingtoneManager
            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUrl)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())

    }

}