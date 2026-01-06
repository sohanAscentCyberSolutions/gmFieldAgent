package com.example.md3.view.commissioning.view


import Status.ERROR
import Status.LOADING
import Status.SUCCESS
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.data.model.commissioning.JourneyVisitRoute.JourneyVisitRouteResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.Route
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.data.model.observation.GetObservationResponse
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.FragmentCurrentVisitBinding
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.KotlinFunctions.calculateTotalDistance
import com.example.md3.utils.Progressive
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.TimePickerUtils
import com.example.md3.utils.ViewKotlinUtils
import com.example.md3.utils.ViewKotlinUtils.toFormattedTime
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.commissioning.observation.view.ObservationBottomSheet
import com.example.md3.view.commissioning.observation.view.ViewObservationBottomSheet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Date

class CurrentVisitFragment : Fragment(), Progressive, OnMapReadyCallback {

    private var _binding: FragmentCurrentVisitBinding? = null
    private val sharedPrefs: SharedPrefs by inject()
    private var latitude: String? = null
    private var longitude: String? = null
    var cameraMoved = false
    private val binding get() = _binding!!
    private val args: CurrentVisitFragmentArgs by navArgs()
    private val commissioningViewModel: CommissioningViewModel by inject()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var getVisitDetailsResponse: GetVisitDetailsResponse
    private lateinit var getVisitObservationResponse: GetObservationResponse
    private var isWorkStarted = false
    private var viewObservation = true
    private var mMap: GoogleMap? = null
    private var totalRoutsDummyDistance = 0.0
    private var mapFragment: SupportMapFragment? = null
    private var journeyIdFromJourneyList = ""
    private var journeyIdFromJourneyListWhichPassInApi = ""

    private val TAG = "CurrentVisitFragment"
    var latLongList: List<Route>? = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        _binding = FragmentCurrentVisitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        observe()
        apiCall()
        clickableViews()
        initMap()
        super.onViewCreated(view, savedInstanceState)
    }
//    private fun initMap(){
//        val mapFragment: SupportMapFragment? =
//            this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//      //  val mapFragment : SupportMapFragment = childFragmentManager.findFragmentByTag("mapFragment") as SupportMapFragment?
//        mapFragment?.getMapAsync(this)
//    }
//    override fun onMapReady(googleMap: GoogleMap) {
//        val totalLatLng = ArrayList<LatLng>()
//        totalLatLng.add(LatLng(28.4679217, 77.0715058))
//        totalLatLng.add(LatLng(28.4697766, 77.0720372))
//        totalLatLng.add(LatLng(28.4722456, 77.0718841))
//
//        val POLYGON_STROKE_WIDTH_PX = 8
//        mMap = googleMap
//
//        val zoom = LatLng(28.4697766, 77.0715058)
//        val polyline1 = mMap.addPolyline(
//            PolylineOptions()
//                .addAll(totalLatLng))
//        polyline1.isVisible = true
//        polyline1.width = POLYGON_STROKE_WIDTH_PX.toFloat()
//        polyline1.color = Color.RED
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoom, 15f))
//
//        var totolDistance = 0.0
//        var lastLatLng : LatLng? = null
//        for (item in totalLatLng){
//            if (lastLatLng == null){
//                lastLatLng = item
//                continue
//            }
//            totolDistance =  totolDistance + calculateDistance(lastLatLng.latitude , lastLatLng.longitude , item.latitude , item.longitude)
//        }
//        Log.e("distance = " ,"$totolDistance  Kilometers")
//
//    }


    private fun initMap() {
        mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this);
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap?.setOnPolylineClickListener({
            Log.e("Polyline tag : ", "== ${it.tag}")
//            val totalvalue = String.format("%.2f", totalRoutsDistance)
            val totalvalue = String.format("%.2f", totalRoutsDummyDistance)
            ViewUtils.showSnackbar(requireView(), "Total All Rout Distance $totalvalue Kilometers")
        });
    }


//    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val theta = lon1 - lon2
//        var dist = (Math.sin(deg2rad(lat1))
//                * Math.sin(deg2rad(lat2))
//                + (Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta))))
//        dist = Math.acos(dist)
//        dist = rad2deg(dist)
//        dist = dist * 60 * 1.1515
//        val distKilo = String.format("%.2f", dist / 0.62137)
//        Log.d(TAG, "calculateDistance: " + distKilo)
//        return distKilo.toDouble()
//    }
//
//    private fun deg2rad(deg: Double): Double {
//        return deg * (Math.PI / 180)
//    }
//
//    private fun rad2deg(rad: Double): Double {
//        return rad * (180 / Math.PI)
//    }


//    private fun calculateTotalDistance(path: List<Path>){
//
//        for (i in 0 until path.size - 1) {
//            val distance = calculateDistance(
//                path[i].latitude, path[i].longitude,
//                path[i + 1].latitude, path[i + 1].longitude
//            )
//            this.totalDistance += distance.toLong()
//        }
//
//    }


    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val roundedLat1 = roundToTwoDecimalPlaces(lat1)
        val roundedLon1 = roundToTwoDecimalPlaces(lon1)
        val roundedLat2 = roundToTwoDecimalPlaces(lat2)
        val roundedLon2 = roundToTwoDecimalPlaces(lon2)

        val theta = roundedLon1 - roundedLon2
        var dist = (Math.sin(deg2rad(roundedLat1))
                * Math.sin(deg2rad(roundedLat2))
                + (Math.cos(deg2rad(roundedLat1))
                * Math.cos(deg2rad(roundedLat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515

        // Convert miles to kilometers
        val distKilo = dist / 0.62137

        // Check for NaN
        if (distKilo.isNaN()) {
            return 0.0
        }
        Log.d(TAG, "calculateDistance: " + distKilo)
        return distKilo
    }


    private fun roundToTwoDecimalPlaces(value: Double): Double {
        return String.format("%.2f", value).toDouble()
    }


    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


    override fun onPause() {
        super.onPause()
    }

    private fun initToolbar() {
        binding.toolbarLayout.toolbar.topAppBar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun apiCall() {
        commissioningViewModel.getVisitDetails(args.caseId)
    }

    private fun observe() {

        sharedViewModel.isRefreshRequiredCommissioning.postValue(true)


        commissioningViewModel.getAllObservation.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    it.data?.let { res ->
                        getVisitObservationResponse = res
                        if (getVisitObservationResponse.count > 0) {
                            binding.timeStatusLayout.stopWorkLayout.btnObservation.text =
                                getString(R.string.view_observation)
                        }
                    }
                }

                ERROR -> {

                }

                LOADING -> {

                }
            }
        }


        sharedViewModel.getWorkTimeLiveData.observe(viewLifecycleOwner) {

            if (::getVisitDetailsResponse.isInitialized && (requireActivity() as MainActivity).isStopwatchRunning) {
                if (getVisitDetailsResponse.status != "Journey Started" && getVisitDetailsResponse.status != "Journey Stopped" && getVisitDetailsResponse.status != "Reached Customer Location") {
                    setTimeStatus(
                        "Total Work Time",
                        it.first,
                        it.second,
                        it.third
                    )
                }
            }
        }

        commissioningViewModel.startJourneyLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        show(false)
                        commissioningViewModel.journeyVisitRoute(
                            it.data?.commissioningVisit!!,
                            JourneyVisitRouteResponse(
                                it.data.id,
                                route = latLongList ?: emptyList()
                            )
                        )
                    }

                    ERROR -> {
                        show(false)
                        if (::getVisitDetailsResponse.isInitialized) {
                            it.message?.let { it1 ->
                                (requireActivity() as MainActivity).anotherCaseAlertDialog(
                                    it1,
                                    "Visit # " + getVisitDetailsResponse.visitCount.toString()
                                )
                            }
                        }
                    }

                    LOADING -> {
                        show(true)
                    }
                }
            }
        }

        commissioningViewModel.journeyVisitRouteLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        show(false)
                        updateUiAccordingToJourney(true)
                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
                        showJourneyStartedDialog(
                            it.data?.visitAddress?.latitude,
                            it.data?.visitAddress?.longitude
                        )
                        commissioningViewModel.getVisitDetails(commissioningVisitId = args.caseId)
                    }

                    ERROR -> {
                        show(false)
                        ViewUtils.showSnackbar(binding.timeStatusLayout.root, it.message)

                    }

                    LOADING -> {
                        show(true)
                    }
                }
            }
        }


        commissioningViewModel.journeyVisitEndRouteLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    show(false)
                    getVisitDetailsResponse.latestJourneyDetails?.let {
                        commissioningViewModel.endJourney(
                            getVisitDetailsResponse.id,
                            Date().toFormattedTime(),
                            it.id
                        )
                    }
                }

                ERROR -> {
                    show(false)
                    ViewUtils.showSnackbar(binding.timeStatusLayout.root, it.message)

                }

                LOADING -> {
                    show(true)
                }
            }
        }



        commissioningViewModel.endJourneyLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        (requireActivity() as MainActivity).stopGettingLocation()
                        commissioningViewModel.getVisitDetails(args.caseId)
                    }

                    ERROR -> {
                        show(false)
                    }

                    LOADING -> {
                        show(true)

                    }
                }
            }
        }


        commissioningViewModel.getStatusChangeRequest.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        show(false)
                        it.data?.let { commissioningViewModel.getVisitDetails(it.id) }
                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = true
                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = true
                    }

                    ERROR -> {
                        show(false)
                    }

                    LOADING -> {
                        show(true)
                    }
                }
            }
        }



        commissioningViewModel.startWorkLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        show(false)
                        it.data?.let { res ->
                            (requireActivity() as MainActivity).startTimeFrom(
                                res.timeElapsed.hours ?: 0, res.timeElapsed.minutes ?: 0
                            )
                            commissioningViewModel.getVisitDetails(res.id)
                        }
                    }

                    ERROR -> {
                        it.message?.let { it1 ->
                            (requireActivity() as MainActivity).anotherCaseAlertDialog(
                                it1,
                                "Visit # " + getVisitDetailsResponse.visitCount.toString()
                            )
                        }
                        show(false)
                    }

                    LOADING -> {
                        show(true)
                    }
                }
            }
        }



        commissioningViewModel.getJourneyWithPathMutableLiveData.observe(viewLifecycleOwner) { resource ->
            if (!resource.isResponseHandled()) {
                when (resource.status) {
                    SUCCESS -> {
                        resource.data?.forEach {
                            it.journeysPathList.let { pathList ->
                                if (pathList.isNotEmpty()) {
                                    val polylineOptions = PolylineOptions().clickable(true)
                                    for (point in pathList) {
                                        polylineOptions.add(LatLng(point.latitude, point.longitude))
                                    }
                                    val polyline = mMap?.addPolyline(polylineOptions)
                                    val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)
                                    polyline?.color = color

                                    var totalDistance = calculateTotalDistance(pathList)
                                    Log.d(TAG, "observe:TotalDistance" + totalDistance)
                                    totalRoutsDummyDistance += totalDistance
                                    binding.distanceTravelled.tvIntro.text = String.format(
                                        "%.2f",
                                        totalRoutsDummyDistance.toDouble()
                                    ) + " Kms"


                                    val startMarker = mMap?.addMarker(
                                        MarkerOptions().position(
                                            LatLng(
                                                pathList.first().latitude,
                                                pathList.first().longitude
                                            )
                                        ).icon(
                                            BitmapDescriptorFactory.fromBitmap(
                                                createStoreMarker("0.0", true)!!
                                            )
                                        )
                                    )


                                    val endMarker = mMap?.addMarker(
                                        MarkerOptions().position(
                                            LatLng(
                                                pathList.last().latitude,
                                                pathList.last().longitude
                                            )
                                        ).icon(
                                            BitmapDescriptorFactory.fromBitmap(
                                                createStoreMarker("$totalDistance", false)!!
                                            )
                                        )
                                    )
                                    startMarker?.showInfoWindow()
                                    endMarker?.showInfoWindow()

                                    if (journeyIdFromJourneyList == journeyIdFromJourneyListWhichPassInApi) {
                                        mMap?.moveCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(
                                                    pathList.first().latitude,
                                                    pathList.first().longitude
                                                ),
                                                15f
                                            )
                                        )
                                        cameraMoved = true
                                    }
                                    polyline?.tag = "$totalDistance"

                                }
                            }
                        }
                        show(false)
                    }

                    ERROR -> {
                        show(false)
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    LOADING -> {
                        show(true)
                    }
                }
            }
        }











        commissioningViewModel.endWorkLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        show(false)
                        KotlinFunctions.removeAllWorkStatus(sharedPrefs)
//                        (requireActivity() as MainActivity).resetStopwatch()
                        (requireActivity() as MainActivity).stopStopwatchAndShowTime(
                            it.data?.timeElapsed?.hours ?: 0, it.data?.timeElapsed?.minutes ?: 0
                        )

                        it.data?.let {
                            commissioningViewModel.getVisitDetails(it.id)
                        }
                    }

                    ERROR -> {
                        show(false)
//                        it.message?.let { it1 ->
//                            (requireActivity() as MainActivity).anotherCaseAlertDialog(
//                                it1,
//                                "Visit # " + getVisitDetailsResponse.visitCount.toString()
//                            )
//                        }
                    }

                    LOADING -> {
                        show(true)
                    }
                }
            }
        }



        sharedViewModel.getCurrentLocation.observe(viewLifecycleOwner) {
            latitude = it.first
            longitude = it.second
        }


        commissioningViewModel.getVisitDetailsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        show(false)
                        it.data?.let { it1 -> handleVisitDetailsResponse(it1) }
                        commissioningViewModel.getAllObservations(args.caseId)
                    }

                    ERROR -> {
                        show(false)
                        ViewUtils.showSnackbar(binding.timeStatusLayout.root, it.message)
                    }

                    LOADING -> {
                        show(true)
                    }
                }
            }
        }






        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("VISIT_INPUT_STATUS")
            ?.observe(viewLifecycleOwner) { result ->
                if (result == "success") {
                    if (::getVisitDetailsResponse.isInitialized) {
                        commissioningViewModel.getVisitDetails(getVisitDetailsResponse.id)
                    }
                }
            }


    }





    fun handleMapAndDistanceVisibility(show: Boolean) {
//        binding.mapFragmentLayout.root.isVisible = show
//        binding.distanceTravelled.root.isVisible = show
//        binding.distanceTravelled.ivNavigate.isVisible = show
//        binding.view2.isVisible = show
//        binding.view1.isVisible = show
        binding.distanceTravelled.tvIntrotitle.text = "Distance Travelled "
        binding.distanceTravelled.tvIntro.text = "0.0"
    }


    private fun handleVisitDetailsResponse(response: GetVisitDetailsResponse) {
        getVisitDetailsResponse = response
        updateUIWithVisitDetails()
    }


    private fun updateUIWithVisitDetails() {

        binding.apply {


            binding.toolbarLayout.toolbar.topAppBar.title =
                getVisitDetailsResponse.serviceRequestDetails.caseId

            instructionLayout.tvIntro.text =
                getVisitDetailsResponse.remark.takeIf { !it.isNullOrEmpty() } ?: "------------"
            visitLocationLayout.tvIntro.text =
                getVisitDetailsResponse.visitAddress.completeAddress.takeIf { it.isNotEmpty() }
                    ?: "------------"

            binding.apply {


                if (getVisitDetailsResponse.status == "Work Started" || getVisitDetailsResponse.status == "Work Stopped" || getVisitDetailsResponse.status == "Reached Customer Location") {

                    binding.timeLayout.tvTotalTimetitle.text = "Total Time Spent"
                    binding.timeLayout.tvTotaljourneyTimeTitle.text = "Total Journey Time"
                    timeLayout.tvTotaljourneyValue.text =
                        "${"%02d".format(getVisitDetailsResponse.totalJourneyHours.hours)}:${
                            "%02d".format(getVisitDetailsResponse.totalJourneyHours.minutes)
                        }:${"%02d".format(0)}"

                    if (getVisitDetailsResponse.status == "Journey Started" || getVisitDetailsResponse.status == "Journey Stopped" || getVisitDetailsResponse.status == "Reached Customer Location") {
                        timeLayout.tvTotalWorkTimeTitle.isVisible = false
                        timeLayout.tvTotalWorkTimeValue.isVisible = false
                    }

                    timeLayout.tvTotalWorkTimeTitle.text = "Total Work time"
                    timeLayout.tvTotalWorkTimeValue.text =
                        "${"%02d".format(getVisitDetailsResponse.timeElapsed.hours)}:${
                            "%02d".format(getVisitDetailsResponse.timeElapsed.minutes)
                        }:${"%02d".format(0)}"


                } else {


                    timeLayout.tvTotalWorkTimeTitle.isVisible = false
                    timeLayout.tvTotalWorkTimeValue.isVisible = false
                    binding.timeLayout.tvTotalTimetitle.text = "Schedule"
                    binding.timeLayout.tvTotaljourneyTimeTitle.text = "Visit Scheduled"
                    binding.timeLayout.tvTotaljourneyValue.text =
                        "${getVisitDetailsResponse.scheduledWorkDate?.takeIf { !it.isNullOrEmpty() } ?: "------------"} ${getVisitDetailsResponse.scheduledWorkTime.takeIf { !it.isNullOrEmpty() } ?: "------------"}"
                    timeLayout.tvTotalWorkTimeValue.text =
                        "${"%02d".format(getVisitDetailsResponse.timeElapsed.hours)}:${
                            "%02d".format(getVisitDetailsResponse.timeElapsed.minutes)
                        }:${"%02d".format(0)}"

//                    binding.timeStatusLayout.btnStartWork.text = getString(R.string.start_work)
                }













                setTimeStatus(
                    "Total Work Time",
                    getVisitDetailsResponse.timeElapsed.hours,
                    getVisitDetailsResponse.timeElapsed.minutes,
                    0
                )


                binding.toolbarLayout.customerInfo.tvCustomerName.text =
                    getVisitDetailsResponse.serviceRequestDetails.customerDetails.name
                binding.toolbarLayout.customerInfo.caseIdValue.text =
                    getVisitDetailsResponse.serviceRequestDetails.caseId




                binding.toolbarLayout.toolbar.tvStatus.isVisible = true
                binding.toolbarLayout.toolbar.tvStatus.text =
                    if (getVisitDetailsResponse.status == "Reached Customer Location") {
                        "Reached Location"
                    } else {
                        getVisitDetailsResponse.status
                    }

                binding.toolbarLayout.toolbar.tvStatus.setChipBackgroundColorResource(
                    KotlinFunctions.getCaseStatusColor(
                        getVisitDetailsResponse.status
                    )
                )

                binding.timeStatusLayout.apply {

                    startJourneyLayout.root.isVisible = false
                    stopJourneyLayout.root.isVisible = false
                    goCaseLayout.root.isVisible = false
                    stopWorkLayout.root.isVisible = false
                    startWorkLayout.root.isVisible = false
                }



                when (getVisitDetailsResponse.status) {

                    "Journey Started" -> {
                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
                        updateUiAccordingToJourney(true)
                    }

                    "Journey Stopped" -> {
                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
                        updateUiAccordingToJourney(false)
                    }

                    "Work Started" -> {
                        callJourneylistApi()
                        updateVisibilityBasedOnWorkState(true)
                    }

                    "Work Stopped" -> {
                        callJourneylistApi()
                        updateVisibilityBasedOnWorkState(false)
                    }

                    "Completed" -> {
                        showCompleteStatus(true)
                    }

                    "Continued" -> {
                        showCompleteStatus(true)
                    }

                    "Scheduled" -> {
                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
                        updateUiAccordingToJourney(false)
                    }

                    "Reached Customer Location" -> {
                        updateVisibilityBasedOnWorkState(false)
                        callJourneylistApi()
                    }
                }
            }
        }
    }


    fun setTimeStatus(title: String, hours: Int, min: Int, sec: Int) {
        binding.timeStatusLayout.totalWorkTimeTitle.text = title
        binding.timeStatusLayout.totalWorkTimeValue.text =
            "${"%02d".format(hours)}:${"%02d".format(min)}:${"%02d".format(sec)}"
    }


    private fun callJourneylistApi() {
        if (::getVisitDetailsResponse.isInitialized) {
//            commissioningViewModel.getJourneyList(args.caseId)
            commissioningViewModel.getJourneyAndResponseWithPath(args.caseId)
        }
    }

    private fun clickableViews() {

        with(binding.visitLocationLayout) {
            ivNavigate.setOnClickListener { view ->
                ViewUtils.openMapIntent(
                    requireActivity(),
                    getVisitDetailsResponse.visitAddress.latitude,
                    getVisitDetailsResponse.visitAddress.longitude
                )
            }
        }

        with(binding.timeStatusLayout) {

            startJourneyLayout.btnStarttReachedLocation.setOnClickListener {
                if ((requireActivity() as MainActivity).checkPermissions()) {
                    (requireActivity() as MainActivity).getLocation()
                    if (::getVisitDetailsResponse.isInitialized) {
                        showConfirmationDialog()
                    }
                } else {
                    (requireActivity() as MainActivity).requestPermissions()
                }
            }






            stopJourneyLayout.btnReachedLocation.setOnClickListener {
                startJourneyLayout.btnStarttReachedLocation.performClick()
            }


            stopWorkLayout.btnObservation.setOnClickListener { view ->
                if (stopWorkLayout.btnObservation.text == getString(R.string.view_observation)) {
                    val bundle = Bundle()
                    bundle.putString(ViewObservationBottomSheet.VISIT_ID, args.caseId)
                    val observationBottomSheet = ViewObservationBottomSheet()
                    observationBottomSheet.arguments = bundle
                    observationBottomSheet.show(childFragmentManager, "")
                } else {
                    showObservationBottomSheet()
                }
            }



            stopWorkLayout.btnStop.setOnClickListener {
                if (::getVisitDetailsResponse.isInitialized) {
                    commissioningViewModel.startAndEndWork(
                        getVisitDetailsResponse.id,
                        null,
                        TimePickerUtils.getCurrentTime()
                    )
                }
            }







            startWorkLayout.btnStartWork.setOnClickListener {
                if (startWorkLayout.btnStartWork.text == getString(R.string.enter_visit_input)) {
                    navigateToVisitFormFragment()
                } else {
                    val hashMap = sharedPrefs.workStatusHashMap
                    if (::getVisitDetailsResponse.isInitialized) {
                        commissioningViewModel.startAndEndWork(
                            getVisitDetailsResponse.id,
                            TimePickerUtils.getCurrentTime(),
                            null
                        )
                    }
                }
            }


            stopWorkLayout.btnSubmitCheckSheet.setOnClickListener {
                if (::getVisitDetailsResponse.isInitialized) {
                    val action =
                        CurrentVisitFragmentDirections.actionCurrentVisitFragmentToSubmitCheckSheetFragment(
                            getVisitDetailsResponse.serviceRequestDetails.id
                        )
                    findNavController().navigate(action)
                }
            }


            startJourneyLayout.btnStartJourney.setOnClickListener {
                show(true)
                lifecycleScope.launch {
                    if ((requireActivity() as MainActivity).startGettingLocation()) {
                        latLongList = null
                        while (latLongList.isNullOrEmpty()) {
                            latLongList = (requireActivity() as MainActivity).getUpdatedLatAndLong()
                            delay(1000)
                        }
                        if (!latLongList.isNullOrEmpty()) {
                            commissioningViewModel.startJourney(
                                getVisitDetailsResponse.id,
                                Date().toFormattedTime()
                            )
                        } else {
                            show(false)
                        }
                    } else {
                        (requireActivity() as MainActivity).startGettingLocation()
                    }
                }
            }



            goCaseLayout.btnGoToCase.setOnClickListener {
                if (::getVisitDetailsResponse.isInitialized) {
                    val action =
                        CurrentVisitFragmentDirections.actionCurrentVisitFragmentToViewOpenCasesFragment(
                            getVisitDetailsResponse.serviceRequestDetails.id,
                            getVisitDetailsResponse.serviceRequestDetails.caseId
                        )
                    findNavController().navigate(action)
                }
            }




            stopJourneyLayout.btnStopJourney.setOnClickListener {
                getVisitDetailsResponse.latestJourneyDetails?.let {
                    val list = (requireActivity() as MainActivity).getUpdatedLatAndLong()
                    commissioningViewModel.journeyVisitEndRoute(
                        getVisitDetailsResponse.id,
                        JourneyVisitRouteResponse(
                            it.id,
                            route = list
                        )
                    )


                }
            }


            stopWorkLayout.btnEnterVisitInput.setOnClickListener {
                navigateToVisitFormFragment()
            }


        }
    }


    private fun createStoreMarker(distance: String, isStart: Boolean): Bitmap? {
        val markerLayout: View = layoutInflater.inflate(R.layout.map_marker, null)
        val markerImage = markerLayout.findViewById<ImageView>(R.id.marker_image) as ImageView
        val markerRating = markerLayout.findViewById<TextView>(R.id.marker_text) as TextView

        if (isStart) {
            markerRating.setTextColor(Color.GREEN)
            markerImage.setImageResource(R.drawable.marker)
            markerRating.text = String.format("%.2f", distance.toDouble()) + " Kms"
        } else {
            markerRating.setTextColor(Color.RED)
            markerImage.setImageResource(R.drawable.racing_flag)
            markerRating.text = String.format("%.2f", distance.toDouble()) + " Kms"
        }

        markerLayout.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        markerLayout.layout(0, 0, markerLayout.measuredWidth, markerLayout.measuredHeight)

        val bitmap = Bitmap.createBitmap(
            markerLayout.measuredWidth,
            markerLayout.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        markerLayout.draw(canvas)
        return bitmap
    }


    fun showJourneyStartedDialog(latitude: String?, longitude: String?) {
        ViewKotlinUtils.navigationAlertDialog(
            requireContext(),
            positiveButtonClickListener = {
            },
            negativeButtonClickListener = {
                ViewUtils.openMapIntent(requireActivity(), latitude, longitude)
            },
            title = "Journey Started",
            subHeading = "Your journey has started successfully. If you would like to navigate to the case’s location, tap “Navigate",
            positiveBtnName = "Okay",
            negativeBtnName = "Navigate"
        )
    }


    fun showConfirmationDialog() {
        ViewKotlinUtils.navigationAlertDialog(
            requireContext(),
            positiveButtonClickListener = {
                commissioningViewModel.changeVisitStatus(
                    args.caseId,
                    "Reached Customer Location",
                    (requireActivity() as MainActivity).latitudeString ?: "",
                    (requireActivity() as MainActivity).longitudeString ?: ""
                )
            },
            negativeButtonClickListener = {

            },
            title = "Reached Customer Location",
            subHeading = "Are you sure you want to proceed? By clicking \"yes,\" you won't be able to create any more",
            positiveBtnName = "Yes",
            negativeBtnName = "No",
            true
        )
    }


    private fun updateVisibilityBasedOnWorkState(isWorkStarted: Boolean) {
        if (isWorkStarted) {
            binding.timeStatusLayout.stopWorkLayout.root.isVisible = true
        } else {
            binding.timeStatusLayout.startWorkLayout.root.isVisible = true
        }

//        with(binding.timeStatusLayout) {
//            btnStop.visibility = if (isWorkStarted) View.VISIBLE else View.GONE
//            btnObservation.visibility = if (isWorkStarted) View.VISIBLE else View.GONE
//            btnSubmitCheckSheet.visibility = if (isWorkStarted) View.VISIBLE else View.GONE
//            btnStartWork.text = if (isWorkStarted) getString(R.string.enter_visit_input) else getString(R.string.start_work)
//            btnStartJourney.visibility = if (isWorkStarted) View.GONE else View.VISIBLE
//        }
    }


    fun distanceBetweenPoints(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371e3 // Earth radius in meters
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)

        val a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return R * c
    }


    private fun showCompleteStatus(isCompleted: Boolean) {
        binding.timeStatusLayout.totalWorkTimeValue.visibility =
            if (isCompleted) View.GONE else View.VISIBLE
        binding.timeStatusLayout.totalWorkTimeTitle.visibility =
            if (isCompleted) View.GONE else View.VISIBLE
        if (isCompleted) {
            binding.timeStatusLayout.goCaseLayout.root.isVisible = true
        }
    }

    private fun updateUiAccordingToJourney(isJourneyStarted: Boolean) {
        if (isJourneyStarted) {
            binding.timeStatusLayout.stopJourneyLayout.root.isVisible = true
            binding.timeStatusLayout.startJourneyLayout.root.isVisible = false
            binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
            binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
        } else {
            binding.timeStatusLayout.stopJourneyLayout.root.isVisible = false
            binding.timeStatusLayout.startJourneyLayout.root.isVisible = true
            if (getVisitDetailsResponse.status == "Scheduled") {
                binding.timeStatusLayout.startJourneyLayout.btnStarttReachedLocation.isVisible =
                    false
            } else {
                binding.timeStatusLayout.startJourneyLayout.btnStarttReachedLocation.isVisible =
                    true
            }
            binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
            binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
        }

        binding.distanceTravelled.root.isVisible = false
        binding.mapFragmentLayout.root.isVisible = false

//        with(binding.timeStatusLayout) {
//            btnStopJourney.visibility = if (isJourneyStarted) View.VISIBLE else View.GONE
//            btnStartJourney.visibility = if (isJourneyStarted) View.GONE else View.VISIBLE
//            btnStartWork.visibility = if (isJourneyStarted) View.GONE else View.VISIBLE
////            btnObservation.visibility = if (isJourneyStarted) View.GONE else View.VISIBLE
////            btnSubmitCheckSheet.visibility = if (isJourneyStarted)  View.GONE else View.VISIBLE
//        }
    }

    private fun showObservationBottomSheet() {
        val observationBottomsheet = ObservationBottomSheet { isSuccess ->
            if (isSuccess) {
                binding.timeStatusLayout.stopWorkLayout.btnObservation.text =
                    getString(R.string.view_observation)
            }
        }
        val bundle = Bundle()
        bundle.putString(
            ObservationBottomSheet.COMMISSIONING_VISIT_ID,
            getVisitDetailsResponse.id
        )
        observationBottomsheet.arguments = bundle
        observationBottomsheet.show(childFragmentManager, "Sheet")
    }

    private fun navigateToVisitFormFragment() {
        if (::getVisitDetailsResponse.isInitialized) {
            val action =
                CurrentVisitFragmentDirections.actionCurrentVisitFragmentToVisitFormFragment(
                    getVisitDetailsResponse.id
                )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMap = null
        _binding = null
    }

    override fun show(show: Boolean) {
        binding.progressBar.isVisible = show
        binding.instructionLayout.root.isVisible = !show
        binding.timeLayout.root.isVisible = !show
        binding.view.isVisible = !show
        binding.view1.isVisible = !show
        binding.visitLocationLayout.root.isVisible = !show
        binding.timeStatusLayout.root.isVisible = !show
        if (::getVisitDetailsResponse.isInitialized) {
            if (getVisitDetailsResponse.status != "Journey Started" && getVisitDetailsResponse.status != "Journey Stopped") {
                binding.distanceTravelled.root.isVisible = !show
                binding.mapFragmentLayout.root.isVisible = !show
                binding.view2.isVisible = !show
                binding.view1.isVisible = !show
            }
        }

    }
}

