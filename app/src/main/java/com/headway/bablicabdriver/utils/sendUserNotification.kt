package com.headway.bablicabdriver.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.headway.bablicabdriver.MainActivity
import com.headway.bablicabdriver.R
import java.util.Random

fun sendNotification(context : Context, title: String, mess: String) {
    val notifyID = System.currentTimeMillis().toInt()
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    try {
        val notifyId = Random().nextInt()
        val pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = "${context.packageName}.Notification" // The id of the channel.

        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.priority = NotificationManager.IMPORTANCE_HIGH
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(mess))
        notificationBuilder.setContentText(mess)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher))
        notificationBuilder.setGroup(channelId)
        notificationBuilder.setTimeoutAfter(4000)


        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mChannel: NotificationChannel = NotificationChannel(channelId, channelId, importance).apply {
            description = mess
            setShowBadge(true)
            vibrationPattern = longArrayOf(0, 250, 250, 250)
            enableLights(false)
        }
        notificationManager.createNotificationChannel(mChannel)
        // Download and set image
        notificationManager.notify(notifyID, notificationBuilder.build())
    }catch (_:Exception) {}
}