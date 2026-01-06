package com.example.md3.baseApplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.view.auth.AuthActivity
import org.koin.java.KoinJavaComponent

class SplashActivity : AppCompatActivity() {

    private val sharedPrefs: SharedPrefs by KoinJavaComponent.inject<SharedPrefs>(SharedPrefs::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {

//        val splashScreen = installSplashScreen()
//        splashScreen.setKeepOnScreenCondition { true }
        super.onCreate(savedInstanceState)

        if (!sharedPrefs.accessToken.isNullOrBlank() && !sharedPrefs.refreshToken.isNullOrBlank()) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@SplashActivity, AuthActivity::class.java)
            startActivity(intent)
        }
        finish()

    }
}