package com.headway.bablicabdriver.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.headway.bablicabdriver.MainActivity
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.model.dashboard.home.RideRequests
import java.util.Random

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

    }
    override fun handleIntent(intent: Intent) {
        Log.d("msg","handleIntent event ${intent.data}")
        Log.d("msg","handleIntent event $intent")


        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")
        val ride_id = intent.getStringExtra("ride_id")
        val booking_type = intent.getStringExtra("booking_type")
        val ride_type = intent.getStringExtra("ride_type")
        val pickup_address = intent.getStringExtra("pickup_address")
        val pickup_Latitude = intent.getStringExtra("pickup_Latitude")
        val pickup_Longitude = intent.getStringExtra("pickup_Longitude")
        val destination_address = intent.getStringExtra("destination_address")
        val destination_Latitude = intent.getStringExtra("destination_Latitude")
        val destination_Longitude = intent.getStringExtra("destination_Longitude")
        val trip_distance = intent.getStringExtra("trip_distance")
        val ride_status = intent.getStringExtra("ride_status")
        val customer_name = intent.getStringExtra("customer_name")
        val customer_profile_image = intent.getStringExtra("customer_profile_image")
        val customer_phone_number = intent.getStringExtra("customer_phone_number")
        val total_price = intent.getStringExtra("total_price")
        val type = intent.getStringExtra("type")


        if (type=="ride_request") {

            val rideRequests = RideRequests(
                ride_id = ride_id,
                booking_type = booking_type,
                ride_type = ride_type,
                pickup_address = pickup_address,
                pickup_Latitude = pickup_Latitude,
                pickup_Longitude = pickup_Longitude,
                destination_address = destination_address,
                destination_Latitude = destination_Latitude,
                destination_Longitude = destination_Longitude,
                trip_distance = trip_distance,
                ride_status = ride_status,
                customer_name = customer_name,
                customer_profile_image = customer_profile_image,
                customer_phone_number = customer_phone_number,
                total_price = total_price,
                type = type
            )
            val broadcastIntent = Intent()
            broadcastIntent.action = "com.notification.ride_request"
            broadcastIntent.putExtra("ride_request",rideRequests)
            sendBroadcast(broadcastIntent)

            sendUserNotification(title = title?:"", mess =  body?:"", rideRequests)
        }



    }

    @SuppressLint("RemoteViewLayout", "RestrictedApi")
    fun sendUserNotification(title: String, mess: String, notificationData: RideRequests) {
        val notifyID = System.currentTimeMillis().toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

//        AppUtils.notificationData = notificationData

        try {

            val soundUri = "android.resource://${packageName}/raw/notification_sound".toUri() // ✅ Fixed URI
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val notifyId = Random().nextInt()
            val pendingIntent = PendingIntent.getActivity(applicationContext, notifyId, intent, PendingIntent.FLAG_IMMUTABLE)
            var channelId = "${packageName}.Notification" // The id of the channel.
            if (notificationData.type == "ride_request") {
                channelId ="${packageName}.ride_request"
            }

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
            notificationBuilder.setContentTitle(title)
            notificationBuilder.setAutoCancel(true)
            notificationBuilder.priority = NotificationManager.IMPORTANCE_HIGH
            notificationBuilder.setContentIntent(pendingIntent)
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(mess))
            notificationBuilder.setContentText(mess)
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
            notificationBuilder.setGroup(channelId)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val mChannel: NotificationChannel
            if (notificationData.type == "ride_request") {
                mChannel = NotificationChannel(channelId, channelId, importance).apply {
                    description = mess
                    setShowBadge(true)
                    vibrationPattern = longArrayOf(0, 250, 250, 250)
                    enableLights(false)
                    setSound(soundUri, audioAttributes)  // ✅ Set custom sound here
                }
            } else {
                mChannel = NotificationChannel(channelId, channelId, importance).apply {
                    description = mess
                    setShowBadge(true)
                    vibrationPattern = longArrayOf(0, 250, 250, 250)
                    enableLights(false)
                }
            }
            notificationManager.createNotificationChannel(mChannel)
            // Download and set image
            notificationManager.notify(notifyID, notificationBuilder.build())
        }catch (_:Exception) {}
    }

}






