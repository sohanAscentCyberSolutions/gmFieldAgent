package com.example.md3.utils.Services

import android.util.Log
import com.example.md3.utils.NotificationUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


//class MyFirebaseMessagingService : FirebaseMessagingService() {
//
//    private val TAG = "MyFirebaseMessagingServ"
//
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        Log.d(TAG, "From: ${remoteMessage.from}")
//
//        // Check if message contains a data payload.
//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
//            remoteMessage.data.let {
//
//                var notificationType = it[ConstantStrings.NOTIFICATION_TYPE]?.toInt() ?: 0
//                val notificationTitle = it[ConstantStrings.NOTIFICATION_TITLE]
//                val notificationMessage = it[ConstantStrings.NOTIFICATION_MESSAGE]
//                val notificationCaseId = it[ConstantStrings.NOTIFICATION_CASE_ID]
//
//                NotificationUtils.sendNotification(
//                    context = this@MyFirebaseMessagingService,
//                    notificationTitle ?: "",
//                    notificationMessage ?: "",
//                    notificationType?.toInt() ?: 0,
//                    notificationCaseId ?: ""
//                )
//            }
//        }
//
//        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//        }
//    }
//
//
//}



class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingServ"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "onMessageReceived:" + remoteMessage.messageType.toString())
        Log.d(TAG, "onMessageReceived:Data" + remoteMessage.data.toString())
        val filteredMap: MutableMap<String?, Any>? = HashMap()

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message remoteMessage Body: " + remoteMessage.getNotification()?.body);
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")


            val dataMap = remoteMessage.data
            Log.d(TAG, "onMessageReceivedDataMap: $dataMap")
            for ((key, value) in dataMap) {
                if (value != null) {
                    filteredMap?.set(key, value)
                }
            }
            Log.d(TAG, "onMessageReceivedDataMap:Final $filteredMap")

            remoteMessage.data.let {
                val notificationTitle = remoteMessage.getNotification()?.title
                val notificationMessage = remoteMessage.getNotification()?.body
                // Display the notification
                NotificationUtils.sendNotification(
                    context = this@MyFirebaseMessagingService,
                    title = notificationTitle ?: "",
                    message = notificationMessage ?: "",
                    filteredMap = filteredMap
                )
            }
        }



        // TODO commented out for testing needed to be implement
        // Check if message contains a notification payload.


        remoteMessage.notification?.let {
            Log.d(TAG, "onMessageReceived: " + it.title)
            Log.d(TAG, "Message Notification Body: ${it.body}")



            // Display the notification even if the app is in foreground
            NotificationUtils.sendNotification(
                context = this@MyFirebaseMessagingService,
                title = it.title ?: "",
                message = it.body ?:  "",
                filteredMap = filteredMap
            )
        }
    }


    override fun onNewToken(token: String) {

        super.onNewToken(token)
    }
}




