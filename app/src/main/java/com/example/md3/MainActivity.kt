package com.example.md3


import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.utils.UploadBottomSheet
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings
import com.example.md3.data.model.commissioning.JourneyVisitRoute.Route
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.ActivityMainBinding
import com.example.md3.utils.AuthUtils
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.NOTIFICATION
import com.example.md3.utils.PickMultiplePhotosContract
import com.example.md3.utils.Services.LocationService
import com.example.md3.utils.Services.StopwatchService
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.UploadViewModel
import com.example.md3.utils.ViewKotlinUtils
import com.example.md3.utils.glide.GlideImageLoader
import com.example.md3.view.auth.AuthActivity
import com.example.md3.view.home.view.HomeDashBoardFragmentDirections
import com.example.md3.view.home.view.HomeFragmentDirections
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.util.Locale


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityMainBinding
    lateinit var navHostFragment: NavHostFragment
    private var locationService: Intent? = null
    private val locationPermissionCode = 2
    private val TAG = "MainActivity"
    private val notificationPermissionCode = 1
    var isStopwatchRunning = false
    var cameraImageUri: Uri? = null
    var latitudeString: String? = null
    var longitudeString: String? = null
    private lateinit var sharedViewModel: SharedViewModel
    private val sharedPrefs: SharedPrefs by inject()
    private val viewModel by viewModels<UploadViewModel>()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val backgroundLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {

            }
        }

    private val locationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            when {
                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        backgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }

                }

                it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                }
            }
        }


    private val imagePickerLauncher = registerForActivityResult(PickMultiplePhotosContract()) {
        viewModel.setSelectedImageList(it.toMutableList())
    }

    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result == true) {
                try {
                    cameraImageUri?.let {
                        viewModel.addSelectedImage(it)
                    }
                } catch (e: IOException) {
                    Log.d(TAG, "Camera Request " + e.localizedMessage)
                    e.printStackTrace()
                }
            }
        }


    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var locationReceiver: BroadcastReceiver
    private lateinit var timeReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


//        setSupportActionBar(binding.toolbar.toolbar)


        locationService = Intent(this, LocationService::class.java)
        setContentView(binding.root)
        initToolbar()
        requestPermissions()
        setupNavController()
        setupDrawerContent(binding.navView)
        updateHeaderView()
        routeNotification()
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }


    private fun setupDrawerContent(navView: NavigationView) {

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.customers_nav -> {
                    // Handle the home action
                    binding.root.closeDrawers()

                    navHostFragment.navController.navigate(R.id.customers_nav)
                }

                R.id.signOut -> {
                    AuthUtils.clearAuthDate()
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    this.finish()
                    binding.root.closeDrawers()
                }


            }
            true
        }


        val menu = navView.menu
        val menuSize = menu.size
        val signOutMenuItem = menu.getItem(menuSize - 1)


        val spannableTitle = SpannableString(signOutMenuItem.title)
        spannableTitle.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.md_theme_error_mediumContrast)),
            0,
            spannableTitle.length,
            0
        )
        signOutMenuItem.title = spannableTitle

    }


    private fun updateHeaderView() {
        val headerView = binding.navView.getHeaderView(0)
        val profileTextView = headerView.findViewById<TextView>(R.id.profile_chip)
        val profileNameTextView = headerView.findViewById<TextView>(R.id.nav_header_title)
        val profilePhoneTextView = headerView.findViewById<TextView>(R.id.nav_header_phone)
        val orgProfile = headerView.findViewById<ImageView>(R.id.orgImg)

        profileTextView.text =
            sharedPrefs.organisationUserName.toCharArray()[0].toUpperCase().toString()
        profileNameTextView.text = sharedPrefs.organisationUserName
        profilePhoneTextView.text = sharedPrefs.organisationUserContact
        GlideImageLoader(this@MainActivity).loadImage(
            Urls.BASE_URL + sharedPrefs.logo,
            orgProfile
        )

    }


    private fun initToolbar() {

        binding.toolbar.apply {
            GlideImageLoader(this@MainActivity).loadImage(
                Urls.BASE_URL + sharedPrefs.logo,
                binding.toolbar.startImageView
            )
            binding.toolbar.endTitleTextView.text = sharedPrefs.organisationUserName
        }

        binding.toolbar.toolbar.setNavigationOnClickListener {
            binding.root.openDrawer(GravityCompat.START)
        }
    }


    fun openDrawer() {
        binding.root.openDrawer(GravityCompat.START)
    }


    private fun setupNavController() {

        navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?)!!

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, args ->
            if (destination.id == R.id.homePageFragment || destination.id == R.id.calendarViewFragment) {
//                binding.toolbar.root.visibility = View.VISIBLE
                binding.bottomNavigationLayout.root.visibility = View.VISIBLE
            } else {
//                binding.toolbar.root.visibility = View.GONE
                binding.bottomNavigationLayout.root.visibility = View.GONE
            }
        }

        binding.bottomNavigationLayout.bottomNavigation.setupWithNavController(navHostFragment.navController)
    }


    private fun routeNotification() {
        val intent = intent
        try {
            val dataMapStr = intent.getStringExtra(ConstantStrings.DATA_MAP)
            Log.d(TAG, "routeNotification: " + dataMapStr)
                val dataMap = KotlinFunctions.convertWithGuava(dataMapStr)
            Log.d(TAG, "routeNotification: " +  dataMap)

            dataMap.let {

                if (dataMap.isNotEmpty() && dataMap.containsKey(ConstantStrings.NOTIFICATION_TYPE)) {


                    val notificationType = dataMap[ConstantStrings.NOTIFICATION_TYPE]
                    val caseID = dataMap[ConstantStrings.NOTIFICATION_CASE_ID] ?: ""
                    val formattedCase = dataMap[ConstantStrings.NOTIFICATION_CASE_FORMATTED_ID] ?: ""


                    when (notificationType) {
                        NOTIFICATION.BREAKDOWN_NEW_CASE.notificationType -> {
                            val action =
                                HomeDashBoardFragmentDirections.actionGlobalViewNewCasesFragment(
                                    caseID,
                                    formattedCase,
                                    "fa37a9da-5add-4711-b6cf-988d958bf6f4"
                                )
                            navHostFragment.navController.navigate(action)
                        }

                        NOTIFICATION.COMMISSIONING_NEW_CASE.notificationType -> {
                            val action = HomeDashBoardFragmentDirections.actionGlobalViewNewCasesFragment(
                                caseID,
                                formattedCase,
                                "fa37a9da-5add-4711-b6cf-988d958bf6f4"
                            )
                            navHostFragment.navController.navigate(action)
                        }

                        NOTIFICATION.COMMISSIONING_UPCOMING_VISIT.notificationType -> {
                            val action =
                                HomeDashBoardFragmentDirections.actionGlobalCurrentVisitFragment("", "")
                            navHostFragment.navController.navigate(action)
                        }

                        NOTIFICATION.BREAKDOWN_UPCOMING_VISIT.notificationType -> {
                            val action =
                                HomeDashBoardFragmentDirections.actionGlobalBreakDownCurrentVisitFragment(
                                    "",
                                    ""
                                )
                            navHostFragment.navController.navigate(action)
                        }

                        else -> {
//                            if (notificationType != 0) {
//                                Toast.makeText(
//                                    this,
//                                    "Invalid Notification Type $notificationType",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
                        }
                    }
                    intent.removeExtra(ConstantStrings.NOTIFICATION_TYPE)
                    intent.removeExtra(ConstantStrings.NOTIFICATION_CASE_ID)
                }

            }

        } catch (e: java.lang.Exception) {
            Log.d(TAG, "routeNotification: " + e.localizedMessage)
            e.printStackTrace()
        }

    }





    private fun isLocationEnabled(): Boolean {

        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    fun anotherCaseAlertDialog(case_id: String, formattedCaseID: String) {
        ViewKotlinUtils.navigationAnotherCaseDialogBox(
            this,
            positiveButtonClickListener = {
                val action = HomeFragmentDirections.actionGlobalCurrentVisitFragment(
                    case_id,
                    formattedCaseID
                )
                findNavController(R.id.nav_host_fragment_content_main).navigate(action)
            },
            negativeButtonClickListener = {

            },
            title = "Another Case Already Active ",
            subHeading = "Another Visit Journey/work is already in progress. Please make sure you stop that journey/work before starting another journey/work",
            positiveBtnName = "Go to Active Case",
            negativeBtnName = "Cancel"
        )
    }


    fun showAlertForLocationError() {
        alertDialog(
            "Ops Something went wrong",
            "There is an some error in your GPS Device " + "Start Journey",
            "Ok",
            "Cancel"
        )
    }

    fun alertDialog(title: String, subHeading: String, positiveBtn: String, negativeBtn: String) {
        ViewKotlinUtils.navigationAnotherCaseDialogBox(
            this,
            positiveButtonClickListener = {

            },
            negativeButtonClickListener = {

            },
            title = title,
            subHeading = subHeading,
            positiveBtnName = positiveBtn,
            negativeBtnName = negativeBtn
        )
    }


    fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            locationPermissionCode
        )
    }


//    fun startGettingLocation(): Boolean {
//        if (checkPermissionsForLocation()) {
//            startService(locationService)
//            return true
//        } else {
//            locationPermissions.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//            return false
//        }
//    }


    fun startGettingLocation(): Boolean {
        if (checkPermissionsForLocation()) {
//            startService(locationService)
            startLocationService()
            return true
        } else {
            locationPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return false
        }
    }


    fun stopGettingLocation() {
        sharedPrefs.latLongList = null
        stopLocationService()
    }


//    fun stopGettingLocation(){
//        sharedPrefs.latLongList = null
//
//        stopService(locationService)
//    }


    fun getUpdatedLatAndLong(): MutableList<Route> {
        sharedPrefs.latLongList?.let {
            Log.d(
                TAG,
                "getUpdatedLatAndLong: " + Gson().fromJson(it, Array<Route>::class.java)
                    .toMutableList()
            )
            return Gson().fromJson(it, Array<Route>::class.java).toMutableList()
        }
        return mutableListOf()
    }


    fun showAlert(title: String, subHeading: String, positiveBtn: String, negativeBtn: String) {
        ViewKotlinUtils.navigationAlertDialog(
            this@MainActivity,
            positiveButtonClickListener = {

            },
            negativeButtonClickListener = {

            },
            title = title,
            subHeading = subHeading,
            positiveBtnName = positiveBtn,
            negativeBtnName = "No",
            true
        )
    }


    private fun checkPermissionsForLocation(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        try {
                            val list: MutableList<Address>? =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)

                            latitudeString = String.format("%.6f", location.latitude)
                            longitudeString = String.format("%.6f", location.longitude)

                            Log.d(
                                "MainActivityLocation",
                                "getLocation: $latitudeString, $longitudeString"
                            )


                            sharedViewModel.setCurrentLocation(
                                Pair(
                                    String.format("%.6f", latitudeString),
                                    String.format("%.6f", longitudeString)
                                )
                            )


//                            list?.get(0)?.let {
//
//                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    fun requestPermissions(rationale: String?, requestCode: Int, vararg permissions: String?) {
        EasyPermissions.requestPermissions(
            this,
            rationale!!,
            requestCode,
            *permissions
        )
    }


    fun hsaCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
        } else {
            EasyPermissions.hasPermissions(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            RC_FILE_READ_AND_CAM_PERM -> {
//                launchPickerDialog()
                showCamera()
            }

            RC_GALLERY_FILE -> {
                pickPhoto()
            }
        }
    }


    fun showUploadSheet() {
        val uploadBottomSheet = UploadBottomSheet()
        uploadBottomSheet.show(supportFragmentManager, "")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            when (requestCode) {
                RC_FILE_READ_AND_CAM_PERM -> {
                    showCamera()
                }

                RC_GALLERY_FILE -> {
                    openGallery()
                }

            }
        }
    }


    fun pickPhoto() = imagePickerLauncher.launch()


    fun openGallery() {
        if (hasGalleryPermission()) {
            pickPhoto()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_read_adn_write),
                RC_GALLERY_FILE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }


    fun showCamera() {
        if (hasCameraAndFilePermission()) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "NewPicture")
                put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
            }

//            cameraImageUri = contentResolver.insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
//            )

            cameraImageUri = createImageUri()

            if (cameraImageUri != null) {
                cameraLauncher.launch(cameraImageUri)
            } else {
                Log.e("Camera", "Failed to create image file")
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_read_and_write_camera),
                RC_FILE_READ_AND_CAM_PERM,
                Manifest.permission.CAMERA
            )
        }
    }


    fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir, "cam_photo.png")
        return FileProvider.getUriForFile(
            applicationContext,
            BuildConfig.APPLICATION_ID + ".fileProvider",
            image
        )
    }


    private fun hasGalleryPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else {
            EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }


    fun hasCameraAndFilePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }


    override fun onResume() {
        super.onResume()


        val filter = IntentFilter("LOCATION_ACTION")
        locationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                var latitude = intent?.getStringExtra(LocationService.LATITUDE)
                var longitude = intent?.getStringExtra(LocationService.LONGITUDE)

                val latitudeString = String.format("%.6f", latitude?.toDouble())
                val longitudeString = String.format("%.6f", longitude?.toDouble())


                val latLongObj = Route(latitude = latitudeString, longitude = longitudeString)


                val gson = Gson()
                val existingListJson = sharedPrefs.latLongList
                val existingList = if (existingListJson != null) {
                    gson.fromJson(existingListJson, Array<Route>::class.java).toMutableList()
                } else {
                    mutableListOf()
                }
                existingList.add(latLongObj)
                val updatedListJson = gson.toJson(existingList)
                sharedPrefs.latLongList = updatedListJson


            }
        }

        registerReceiver(locationReceiver, filter, RECEIVER_NOT_EXPORTED)







        getStopwatchStatus()

        // Receiving stopwatch status from service
        val statusFilter = IntentFilter()
        statusFilter.addAction(StopwatchService.STOPWATCH_STATUS)
        statusReceiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(p0: Context?, p1: Intent?) {
                val isRunning = p1?.getBooleanExtra(StopwatchService.IS_STOPWATCH_RUNNING, false)!!
                isStopwatchRunning = isRunning
                val timeElapsed = p1.getIntExtra(StopwatchService.TIME_ELAPSED, 0)

//                updateLayout(isStopwatchRunning)
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(statusReceiver, statusFilter)


        // Receiving time values from service
        val timeFilter = IntentFilter()
        timeFilter.addAction(StopwatchService.STOPWATCH_TICK)
        timeReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val timeElapsed = p1?.getIntExtra(StopwatchService.TIME_ELAPSED, 0)!!
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(timeReceiver, timeFilter)


    }


    @SuppressLint("SetTextI18n")
    private fun updateStopwatchValue(timeElapsed: Int) {
        val hours: Int = (timeElapsed / 60) / 60
        val minutes: Int = timeElapsed / 60
        val seconds: Int = timeElapsed % 60
        sharedViewModel.setWorkTime(hours = hours, minutes = minutes, seconds = seconds)
    }


    override fun onStart() {
        super.onStart()

        // Moving the service to background when the app is visible
        moveToBackground()
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(locationReceiver)
        unregisterReceiver(statusReceiver)
        unregisterReceiver(timeReceiver)

        // Moving the service to foreground when the app is in background / not visible
        moveToForeground()
    }


    fun startLocationService() {
        val locationServiceIntent = Intent(this, LocationService::class.java)
        locationServiceIntent.action = LocationService.ACTION_START_SERVICE
        startService(locationServiceIntent)
    }

    fun stopLocationService() {
        val locationServiceIntent = Intent(this, LocationService::class.java)
        locationServiceIntent.action = LocationService.ACTION_STOP_SERVICE
        stopService(locationServiceIntent)
    }


    fun startStopwatch() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.START)
        startService(stopwatchService)
    }

    fun pauseStopwatch() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.PAUSE)
        startService(stopwatchService)
    }

    fun resetStopwatch() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.RESET)
        startService(stopwatchService)
    }


    private fun getStopwatchStatus() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.GET_STATUS)
        startService(stopwatchService)
    }

    private fun moveToForeground() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(
            StopwatchService.STOPWATCH_ACTION,
            StopwatchService.MOVE_TO_FOREGROUND
        )
        startService(stopwatchService)
    }

    private fun moveToBackground() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(
            StopwatchService.STOPWATCH_ACTION,
            StopwatchService.MOVE_TO_BACKGROUND
        )
        startService(stopwatchService)
    }


    fun startTimeFrom(hours: Int, minutes: Int) {
        val totalSeconds = hours * 3600 + minutes * 60
        val intent = Intent(this, StopwatchService::class.java)
        intent.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.START_FROM_TIME)
        intent.putExtra(StopwatchService.START_TIME_SECONDS, totalSeconds)
        startService(intent)
    }


    fun stopStopwatchAndShowTime(hours: Int, minutes: Int) {
        val totalSeconds = hours * 3600 + minutes * 60
        val intent = Intent(this, StopwatchService::class.java)
        intent.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.STOP)
        intent.putExtra(StopwatchService.START_TIME_SECONDS, totalSeconds)
        startService(intent)
    }

    companion object {
        const val RC_GALLERY_FILE = 111
        const val RC_FILE_READ_AND_CAM_PERM = 121
        const val RC_PUSH_NOTIFICATION = 122
    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }


    override fun onDestroy() {
        super.onDestroy()
    }


}





