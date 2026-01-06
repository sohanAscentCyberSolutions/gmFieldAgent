package com.example.md3.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings.DATA_MAP
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.view.auth.AuthActivity


object NotificationUtils {

    private const val CHANNEL_ID = "GLADMIND_MAIN_CHANNEL_ID"
    private const val CHANNEL_NAME = "Gladmind Push Notification"
    private const val CHANNEL_DESCRIPTION = "Case's detail's"


    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // Create Notification Channel for Android Oreo and above
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
//        }
    }



    fun sendNotification(context: Context, title: String, message: String , filteredMap : MutableMap<String?, Any>? ) {
        createNotificationChannel(context)
        val sharedPrefs = SharedPrefs(context)
        val intent : Intent?
        if(sharedPrefs.accessToken != null){
            intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(DATA_MAP, KotlinFunctions.convertWithGuava(filteredMap))
        }else{
            intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)


        // Create notification
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
