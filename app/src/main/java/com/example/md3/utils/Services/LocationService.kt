package com.example.md3.utils.Services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.md3.MainActivity
import com.example.md3.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


//class LocationService() : Service() {
//
//
//    companion object {
//        const val CHANNEL_ID = "12345"
//        const val NOTIFICATION_ID=12345
//        const val LATITUDE = "latitude"
//        const val LONGITUDE = "longitude"
//    }
//
//    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
//    private var locationCallback: LocationCallback? = null
//    private var locationRequest: LocationRequest? = null
//
//    private var notificationManager: NotificationManager? = null
//
//    private var location: Location?=null
//
//    override fun onCreate() {
//        super.onCreate()
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).setIntervalMillis(500).build()
//        locationCallback = object : LocationCallback() {
//            override fun onLocationAvailability(p0: LocationAvailability) {
//                super.onLocationAvailability(p0)
//            }
//
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//                onNewLocation(locationResult)
//            }
//        }
//    }
//
//    @Suppress("MissingPermission")
//    fun createLocationRequest(){
//        try {
//            fusedLocationProviderClient?.requestLocationUpdates(
//                locationRequest!!,locationCallback!!,null
//            )
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//
//    }
//
//    private fun removeLocationUpdates(){
//        locationCallback?.let {
//            fusedLocationProviderClient?.removeLocationUpdates(it)
//        }
//        stopForeground(true)
//        stopSelf()
//    }
//
//
//
//    private fun onNewLocation(locationResult: LocationResult) {
//        location = locationResult.lastLocation
//        val statusIntent = Intent("LOCATION_ACTION")
//        statusIntent.putExtra(LATITUDE, location?.latitude.toString())
//        statusIntent.putExtra(LONGITUDE, location?.longitude.toString())
//        sendBroadcast(statusIntent)
//    }
//
//
////    private fun sendMessageToActivity(l: Location, msg: String) {
////        val intent = Intent("GPSLocationUpdates")
////        intent.putExtra("Status", msg)
////        val b = Bundle()
////        b.putParcelable("Location", l)
////        intent.putExtra("Location", b)
////        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
////    }
//
//
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        super.onStartCommand(intent, flags, startId)
//        createLocationRequest()
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent): IBinder? = null
//
//    override fun onDestroy() {
//        super.onDestroy()
//        removeLocationUpdates()
//    }
//}


//import android.app.*
//import android.content.Intent
//import android.location.Location
//import android.os.Build
//import android.os.IBinder
//import androidx.annotation.RequiresApi
//import com.example.md3.MainActivity
//import com.example.md3.R
//import com.google.android.gms.location.*

class LocationService : Service() {

    companion object {
        const val CHANNEL_ID = "Location_Notifications"
        const val NOTIFICATION_ID = 12345
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    }

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null

    private var notificationManager: NotificationManager? = null

    private var location: Location? = null

    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .build()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { onNewLocation(it) }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_START_SERVICE -> {
                startLocationService()
            }

            ACTION_STOP_SERVICE -> {
                stopLocationService()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdates()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Location Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Service")
            .setContentText("Location updates are running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    @Suppress("MissingPermission")
    private fun createLocationRequest() {
        try {
            locationRequest?.let {
                locationCallback?.let { it1 ->
                    fusedLocationProviderClient?.requestLocationUpdates(
                        it,
                        it1,
                        null
                    )
                }
            }
            startForeground(NOTIFICATION_ID, buildNotification())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeLocationUpdates() {
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
        stopForeground(true)
        stopSelf()
    }

    private fun onNewLocation(newLocation: Location) {
        location = newLocation
        val statusIntent = Intent("LOCATION_ACTION")
        statusIntent.putExtra(LATITUDE, location?.latitude.toString())
        statusIntent.putExtra(LONGITUDE, location?.longitude.toString())
        sendBroadcast(statusIntent)
    }

    private fun startLocationService() {
        createNotificationChannel()
        createLocationRequest()
    }

    private fun stopLocationService() {
        removeLocationUpdates()
    }
}



