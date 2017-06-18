package com.ringerjk.itechgetmeoxygen.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ringerjk.itechgetmeoxygen.R
import com.ringerjk.itechgetmeoxygen.ui.MainActivity




/**
 * Created by Yury Kanetski on 6/17/17.
 */

class NotificationGcmManager : FirebaseMessagingService(){

    override fun onMessageReceived(message: RemoteMessage?) {
        sendNotification(message?.data?.get("Title"), message?.data?.get("body"))
    }

    private fun sendNotification(title: String?, body: String?){
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pattern = longArrayOf(500, 500, 500, 500, 500)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}