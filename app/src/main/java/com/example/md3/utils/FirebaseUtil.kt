package com.example.md3.utils


import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseUtil {

    private const val TAG = "FirebaseUtil"

    fun getFCMToken(onTokenReceive : (token : String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                onTokenReceive(null)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "FCM Token: $token")
            onTokenReceive(token)
        }
    }
}