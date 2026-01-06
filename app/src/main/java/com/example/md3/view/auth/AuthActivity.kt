package com.example.md3.view.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.data.model.auth.AuthData
import com.example.md3.data.model.user_profile.UserDetails
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.ActivityAuthBinding
import com.example.md3.utils.AuthUtils
import com.example.md3.utils.FirebaseUtil
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class AuthActivity : AppCompatActivity() {
    private  val TAG = "AuthActivity"
    lateinit var binding: ActivityAuthBinding
    lateinit var navHostFragment: NavHostFragment
    private val sharedPrefs: SharedPrefs by inject()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Snackbar.make(
                binding.root,
                String.format(
                    String.format(
                        "Notification Permission",
                        getString(R.string.app_name)
                    )
                ),
                Snackbar.LENGTH_SHORT
            ).setAction("Go to settings") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    startActivity(settingsIntent)
                }
            }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        askNotificationPermission()
        setContentView(binding.root)
    }


    private fun initViews() {
        binding = ActivityAuthBinding.inflate(layoutInflater, null, false)
        navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
    }



    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }




    fun setAuthTokensAndAuthData(authData: AuthData) {
        AuthUtils.setAuthTokensAndAuthData(authData = authData)
        setFCMToken()
    }


    private fun setFCMToken(){
        //TODO API NEEDED TO SET FCM TOKEN
        FirebaseUtil.getFCMToken { token ->
            Log.d(TAG, "setFCMToken:" + token)
        }
    }

    fun setOrgUserID(data: UserDetails?) {
        AuthUtils.setOrganisationUserId(data)
    }

    fun startMainActivity() {
        val intent = Intent(this@AuthActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}