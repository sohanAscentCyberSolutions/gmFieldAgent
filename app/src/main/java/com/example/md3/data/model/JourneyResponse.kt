package com.example.md3.data.model


import com.google.gson.annotations.SerializedName

data class JourneyResponse(
    @SerializedName("journeys")
    val journeys: List<Journey>
)



data class JourneyPathResponse(
    @SerializedName("journeys")
    val journeys: List<JourneyPathResult>
)


data class JourneyPathResult(
    val journeyId : String,
    val journeysPathList: List<Path>
)







//
//
//package com.example.md3.view.commissioning.view
//
//
//import Status
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import com.example.md3.MainActivity
//import com.example.md3.R
//import com.example.md3.data.model.commissioning.JourneyVisitRoute.JourneyVisitRouteResponse
//import com.example.md3.data.model.commissioning.JourneyVisitRoute.Route
//import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
//import com.example.md3.data.model.observation.GetObservationResponse
//import com.example.md3.data.preferences.SharedPrefs
//import com.example.md3.databinding.FragmentCurrentVisitBinding
//import com.example.md3.utils.KotlinFunctions
//import com.example.md3.utils.Progressive
//import com.example.md3.utils.SharedViewModel
//import com.example.md3.utils.TimePickerUtils
//import com.example.md3.utils.ViewKotlinUtils
//import com.example.md3.utils.ViewKotlinUtils.toFormattedTime
//import com.example.md3.utils.ViewUtils
//import com.example.md3.view.commissioning.CommissioningViewModel
//import com.example.md3.view.commissioning.observation.view.ObservationBottomSheet
//import com.example.md3.view.commissioning.observation.view.ViewObservationBottomSheet
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.gms.maps.model.PolylineOptions
//import com.google.gson.internal.bind.MapTypeAdapterFactory
//import org.koin.android.ext.android.inject
//import java.util.Date
//
//
//class CurrentVisitFragment : Fragment(), Progressive, OnMapReadyCallback {
//
//    private var _binding: FragmentCurrentVisitBinding? = null
//    private val sharedPrefs: SharedPrefs by inject()
//    private var latitude: String? = null
//    private var longitude: String? = null
//    private val binding get() = _binding!!
//    private val args: CurrentVisitFragmentArgs by navArgs()
//    private val commissioningViewModel: CommissioningViewModel by inject()
//    private lateinit var sharedViewModel: SharedViewModel
//    private lateinit var getVisitDetailsResponse: GetVisitDetailsResponse
//    private lateinit var getVisitObservationResponse: GetObservationResponse
//    private var isWorkStarted = false
//    private var viewObservation = true
//    private lateinit var mMap: GoogleMap
//    private var totalRoutsDistance = 0.0
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
//        _binding = FragmentCurrentVisitBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        initToolbar()
//        observe()
//        apiCall()
//        clickableViews()
//        initMap()
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//    private fun initMap() {
//        val mapFragment: SupportMapFragment? =
//            this.childFragmentManager
//                .findFragmentById(R.id.map) as SupportMapFragment
//        //  val mapFragment : SupportMapFragment = childFragmentManager.findFragmentByTag("mapFragment") as SupportMapFragment?
//        mapFragment?.getMapAsync(this)
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
//
//        val LatLngListData = ArrayList<ArrayList<LatLng>>()
//
//        val totalLatLng1 = ArrayList<LatLng>()
//        totalLatLng1.add(LatLng(28.4563053, 77.0694552))
//        totalLatLng1.add(LatLng(28.4579881, 77.0711709))
//        totalLatLng1.add(LatLng(28.4581455, 77.0711501))
//        totalLatLng1.add(LatLng(28.4595868, 77.0688994))
//
//
//        val totalLatLng2 = ArrayList<LatLng>()
//        totalLatLng2.add(LatLng(28.4568032, 77.0699873))
//        totalLatLng2.add(LatLng(28.4578812, 77.0712188))
//        totalLatLng2.add(LatLng(28.4585151, 77.0728345))
//
//        LatLngListData.add(totalLatLng1)
//        LatLngListData.add(totalLatLng2)
//
//        var id = 1
//        for (data in LatLngListData) {
//            drawRout(data, "$id")
//            id = id + 1
//        }
//        mMap.setOnPolylineClickListener(GoogleMap.OnPolylineClickListener {
//            Log.e("Polyline tag : ", "== ${it.tag}")
//            val totalvalue = String.format("%.2f", totalRoutsDistance)
//            ViewUtils.showSnackbar(requireView(), "Total All Rout Distance $totalvalue Kilometers")
//        });
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngListData[0][0], 15f))
//    }
//
//    private fun createStoreMarker(distance: String, isStart: Boolean): Bitmap? {
//        val markerLayout: View = layoutInflater.inflate(R.layout.map_marker, null)
//        val markerImage = markerLayout.findViewById<ImageView>(R.id.marker_image) as ImageView
//        val markerRating =  markerLayout.findViewById<TextView>(R.id.marker_text) as TextView
//
//        if (isStart) {
//            markerRating.setTextColor(Color.RED)
//            markerImage.setImageResource(R.drawable.marker)
//        } else {
//            markerRating.setTextColor(Color.BLUE)
//            markerImage.setImageResource(R.drawable.racing_flag)
//        }
//        markerRating.text = "$distance KM"
//        markerLayout.measure(
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        )
//        markerLayout.layout(0, 0, markerLayout.measuredWidth, markerLayout.measuredHeight)
//        val bitmap = Bitmap.createBitmap(
//            markerLayout.measuredWidth,
//            markerLayout.measuredHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        markerLayout.draw(canvas)
//        return bitmap
//    }
//
//    private fun drawRout(LatLngList: ArrayList<LatLng>, id: String) {
//        val POLYGON_STROKE_WIDTH_PX = 16
//        requireActivity()!!.runOnUiThread {
//            val polyline1 = mMap.addPolyline(
//                PolylineOptions()
//                    .clickable(true)
//                    .addAll(LatLngList)
//            )
//            polyline1.isVisible = true
//            polyline1.width = POLYGON_STROKE_WIDTH_PX.toFloat()
//            val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)
//            polyline1.color = color
//            polyline1.tag = id
//            Log.e("Polyline tag Added: ", "== ${polyline1.tag}")
//
//            var totolDistance = 0.0
//            var lastLatLng: LatLng? = null
//            for (item in LatLngList) {
//                if (lastLatLng == null) {
//                    lastLatLng = item
//                    continue
//                }
//                totolDistance = totolDistance + calculateDistance(
//                    lastLatLng.latitude,
//                    lastLatLng.longitude,
//                    item.latitude,
//                    item.longitude
//                )
//            }
//            totalRoutsDistance = totalRoutsDistance + totolDistance
//
//            val marker = mMap.addMarker(
//                MarkerOptions().position(LatLngList.get(0)).icon(
//                    BitmapDescriptorFactory.fromBitmap(
//                        createStoreMarker("0.0", true)!!
//                    )
//                )
//            )
//            marker!!
//                .showInfoWindow()
//            val marker1 = mMap.addMarker(
//                MarkerOptions().position(LatLngList.get(LatLngList.size - 1)).icon(
//                    BitmapDescriptorFactory.fromBitmap(
//                        createStoreMarker("$totolDistance", false)!!
//                    )
//                )
//            )
//            marker1!!
//                .showInfoWindow()
//        }
//    }
//
//    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val theta = lon1 - lon2
//        var dist = (Math.sin(deg2rad(lat1))
//                * Math.sin(deg2rad(lat2))
//                + (Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta))))
//        dist = Math.acos(dist)
//        dist = rad2deg(dist)
//        dist *= 60 * 1.1515
//        val distKilo = String.format("%.2f", dist / 0.62137)
//        return distKilo.toDouble()
//    }
//
//    private fun deg2rad(deg: Double): Double {
//        return deg * Math.PI / 180.0
//    }
//
//    private fun rad2deg(rad: Double): Double {
//        return rad * 180.0 / Math.PI
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    private fun initToolbar() {
//        binding.toolbarLayout.toolbar.topAppBar.setOnClickListener {
//            findNavController().popBackStack()
//        }
//    }
//
//    private fun apiCall() {
//        commissioningViewModel.getVisitDetails(args.caseId)
//    }
//
//    private fun observe() {
//
//        sharedViewModel.isRefreshRequiredCommissioning.postValue(true)
//
//
//        commissioningViewModel.getAllObservation.observe(viewLifecycleOwner) {
//            when (it.status) {
//                Status.SUCCESS -> {
//                    it.data?.let { res ->
//                        getVisitObservationResponse = res
//                        if (getVisitObservationResponse.count > 0) {
//                            binding.timeStatusLayout.stopWorkLayout.btnObservation.text =
//                                getString(R.string.view_observation)
//                        }
//                    }
//                }
//
//                Status.ERROR -> {
//
//                }
//
//                Status.LOADING -> {
//
//                }
//            }
//        }
//
//
//        sharedViewModel.getWorkTimeLiveData.observe(viewLifecycleOwner) {
//
//            if (::getVisitDetailsResponse.isInitialized && (requireActivity() as MainActivity).isStopwatchRunning) {
//                if (getVisitDetailsResponse.status != "Journey Started" && getVisitDetailsResponse.status != "Journey Stopped" && getVisitDetailsResponse.status != "Reached Customer Location") {
//                    setTimeStatus(
//                        "Total Work Time",
//                        it.first,
//                        it.second,
//                        it.third
//                    )
//                }
//            }
//        }
//
//        commissioningViewModel.startJourneyLiveData.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(false)
//                        commissioningViewModel.journeyVisitRoute(
//                            it.data?.commissioningVisit!!,
//                            JourneyVisitRouteResponse(
//                                it.data.id,
//                                route = listOf(Route(latitude ?: "", longitude ?: ""))
//                            ),
//                        )
//                    }
//
//                    Status.ERROR -> {
//                        show(false)
//                        if (::getVisitDetailsResponse.isInitialized) {
//                            it.message?.let { it1 ->
//                                (requireActivity() as MainActivity).anotherCaseAlertDialog(
//                                    it1,
//                                    "Visit # " + getVisitDetailsResponse.visitCount.toString()
//                                )
//                            }
//                        }
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//                    }
//                }
//            }
//        }
//
//        commissioningViewModel.journeyVisitRouteLiveData.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(false)
//                        updateUiAccordingToJourney(true)
//                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
//                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
//                        showJourneyStartedDialog(
//                            it.data?.visitAddress?.latitude,
//                            it.data?.visitAddress?.longitude
//                        )
//                        commissioningViewModel.getVisitDetails(commissioningVisitId = args.caseId)
//                    }
//
//                    Status.ERROR -> {
//                        show(false)
//                        ViewUtils.showSnackbar(binding.timeStatusLayout.root, it.message)
//
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//                    }
//                }
//            }
//        }
//
//        commissioningViewModel.endJourneyLiveData.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(true)
//                        KotlinFunctions.removeAllJourneyStatus(sharedPrefs)
//                        updateUiAccordingToJourney(false)
//                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = true
//                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = true
//                        commissioningViewModel.getVisitDetails(args.caseId)
//                    }
//
//                    Status.ERROR -> {
//                        show(false)
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//
//                    }
//                }
//            }
//        }
//
//
//        commissioningViewModel.getStatusChangeRequest.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(false)
//                        it.data?.let { commissioningViewModel.getVisitDetails(it.id) }
//                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = true
//                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = true
//                    }
//
//                    Status.ERROR -> {
//                        show(false)
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//                    }
//                }
//            }
//        }
//
//
//
//        commissioningViewModel.startWorkLiveData.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(false)
//                        it.data?.let { res ->
//                            (requireActivity() as MainActivity).startTimeFrom(
//                                res.timeElapsed.hours ?: 0, res.timeElapsed.minutes ?: 0
//                            )
//                            commissioningViewModel.getVisitDetails(res.id)
//                        }
//                    }
//
//                    Status.ERROR -> {
//                        it.message?.let { it1 ->
//                            (requireActivity() as MainActivity).anotherCaseAlertDialog(
//                                it1,
//                                "Visit # " + getVisitDetailsResponse.visitCount.toString()
//                            )
//                        }
//                        show(false)
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//                    }
//                }
//            }
//        }
//
//
//
//        commissioningViewModel.endWorkLiveData.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(false)
//                        KotlinFunctions.removeAllWorkStatus(sharedPrefs)
////                        (requireActivity() as MainActivity).resetStopwatch()
//                        (requireActivity() as MainActivity).stopStopwatchAndShowTime(
//                            it.data?.timeElapsed?.hours ?: 0, it.data?.timeElapsed?.minutes ?: 0
//                        )
//
//                        it.data?.let {
//                            commissioningViewModel.getVisitDetails(it.id)
//                        }
//                    }
//
//                    Status.ERROR -> {
//                        show(false)
////                        it.message?.let { it1 ->
////                            (requireActivity() as MainActivity).anotherCaseAlertDialog(
////                                it1,
////                                "Visit # " + getVisitDetailsResponse.visitCount.toString()
////                            )
////                        }
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//                    }
//                }
//            }
//        }
//
//
//
//        sharedViewModel.getCurrentLocation.observe(viewLifecycleOwner) {
//            latitude = it.first
//            longitude = it.second
//        }
//
//
//        commissioningViewModel.getVisitDetailsMutableLiveData.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(false)
//                        it.data?.let { it1 -> handleVisitDetailsResponse(it1) }
//                        commissioningViewModel.getAllObservations(args.caseId)
//                    }
//
//                    Status.ERROR -> {
//                        show(false)
//                        ViewUtils.showSnackbar(binding.timeStatusLayout.root, it.message)
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//                    }
//                }
//            }
//        }
//
//
//        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("VISIT_INPUT_STATUS")
//            ?.observe(viewLifecycleOwner) { result ->
//                if (result == "success") {
//                    if (::getVisitDetailsResponse.isInitialized) {
//                        commissioningViewModel.getVisitDetails(getVisitDetailsResponse.id)
//                    }
//                }
//            }
//
//
//    }
//
//    private fun handleVisitDetailsResponse(response: GetVisitDetailsResponse) {
//        getVisitDetailsResponse = response
//        updateUIWithVisitDetails()
//    }
//
//    private fun updateUIWithVisitDetails() {
//
//        binding.apply {
//
//
//            binding.toolbarLayout.toolbar.topAppBar.title =
//                getVisitDetailsResponse.serviceRequestDetails.caseId
//
//            instructionLayout.tvIntro.text =
//                getVisitDetailsResponse.remark.takeIf { !it.isNullOrEmpty() } ?: "------------"
//            visitLocationLayout.tvIntro.text =
//                getVisitDetailsResponse.visitAddress.completeAddress.takeIf { it.isNotEmpty() }
//                    ?: "------------"
//
//            binding.apply {
//
//                if (getVisitDetailsResponse.status == "Journey Started") {
//                    timeLayout.tvTotaljourneyValue.text =
//                        getVisitDetailsResponse.scheduledWorkTime?.takeIf { it.isNotEmpty() }
//                            ?: "------------"
//                    timeLayout.tvTotalWorkTimeValue.text =
//                        getVisitDetailsResponse.scheduledWorkTime?.takeIf { it.isNotEmpty() }
//                            ?: "------------"
//                } else {
//                    binding.timeLayout.tvTotalTimetitle.text = "Schedule"
//                    binding.timeLayout.tvTotaljourneyTimeTitle.text = "Visit Scheduled"
//                    binding.timeLayout.tvTotaljourneyValue.text =
//                        "${getVisitDetailsResponse.scheduledWorkDate?.takeIf { !it.isNullOrEmpty() } ?: "------------"} ${getVisitDetailsResponse.scheduledWorkTime.takeIf { !it.isNullOrEmpty() } ?: "------------"}"
//                    timeLayout.tvTotalWorkTimeValue.text =
//                        "${"%02d".format(getVisitDetailsResponse.timeElapsed.hours)}:${
//                            "%02d".format(getVisitDetailsResponse.timeElapsed.minutes)
//                        }:${"%02d".format(0)}"
//
////                    binding.timeStatusLayout.btnStartWork.text = getString(R.string.start_work)
//                }
//
//                setTimeStatus(
//                    "Total Work Time",
//                    getVisitDetailsResponse.timeElapsed.hours,
//                    getVisitDetailsResponse.timeElapsed.minutes,
//                    0
//                )
//
//
//                binding.toolbarLayout.customerInfo.tvCustomerName.text =
//                    getVisitDetailsResponse.serviceRequestDetails.customerDetails.name
//                binding.toolbarLayout.customerInfo.caseIdValue.text =
//                    getVisitDetailsResponse.serviceRequestDetails.caseId
//
//
//
//
//                binding.toolbarLayout.toolbar.tvStatus.isVisible = true
//                binding.toolbarLayout.toolbar.tvStatus.text =
//                    if (getVisitDetailsResponse.status == "Reached Customer Location") {
//                        "Reached Location"
//                    } else {
//                        getVisitDetailsResponse.status
//                    }
//
//                binding.toolbarLayout.toolbar.tvStatus.setChipBackgroundColorResource(
//                    KotlinFunctions.getCaseStatusColor(
//                        getVisitDetailsResponse.status
//                    )
//                )
//
//                binding.timeStatusLayout.apply {
//
//                    startJourneyLayout.root.isVisible = false
//                    stopJourneyLayout.root.isVisible = false
//                    goCaseLayout.root.isVisible = false
//                    stopWorkLayout.root.isVisible = false
//                    startWorkLayout.root.isVisible = false
//                }
//
//
//
//                when (getVisitDetailsResponse.status) {
//
//                    "Journey Started" -> {
//                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
//                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
//                        updateUiAccordingToJourney(true)
//                    }
//
//                    "Journey Stopped" -> {
//                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
//                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
//                        updateUiAccordingToJourney(false)
//                    }
//
//                    "Work Started" -> {
//                        updateVisibilityBasedOnWorkState(true)
//                    }
//
//                    "Work Stopped" -> {
//                        updateVisibilityBasedOnWorkState(false)
//                    }
//
//                    "Completed" -> {
//                        showCompleteStatus(true)
//                    }
//
//                    "Continued" -> {
//                        showCompleteStatus(true)
//                    }
//
//                    "Scheduled" -> {
//                        binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
//                        binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
//                        updateUiAccordingToJourney(false)
//                    }
//
//                    "Reached Customer Location" -> {
//                        updateVisibilityBasedOnWorkState(false)
//                    }
//                }
//            }
//        }
//    }
//
//
//    fun setTimeStatus(title: String, hours: Int, min: Int, sec: Int) {
//        binding.timeStatusLayout.totalWorkTimeTitle.text = title
//        binding.timeStatusLayout.totalWorkTimeValue.text =
//            "${"%02d".format(hours)}:${"%02d".format(min)}:${"%02d".format(sec)}"
//    }
//
//    private fun clickableViews() {
//
//        with(binding.visitLocationLayout) {
//            ivNavigate.setOnClickListener { view ->
//                ViewUtils.openMapIntent(
//                    requireActivity(),
//                    getVisitDetailsResponse.visitAddress.latitude,
//                    getVisitDetailsResponse.visitAddress.longitude
//                )
//            }
//        }
//
//        with(binding.timeStatusLayout) {
//
//            startJourneyLayout.btnStarttReachedLocation.setOnClickListener {
//                if (::getVisitDetailsResponse.isInitialized) {
//                    showConfirmationDialog()
//                }
//            }
//
//            stopJourneyLayout.btnReachedLocation.setOnClickListener {
//                startJourneyLayout.btnStarttReachedLocation.performClick()
//            }
//
//
//            stopWorkLayout.btnObservation.setOnClickListener { view ->
//                if (stopWorkLayout.btnObservation.text == getString(R.string.view_observation)) {
//                    val bundle = Bundle()
//                    bundle.putString(ViewObservationBottomSheet.VISIT_ID, args.caseId)
//                    val observationBottomSheet = ViewObservationBottomSheet()
//                    observationBottomSheet.arguments = bundle
//                    observationBottomSheet.show(childFragmentManager, "")
//                } else {
//                    showObservationBottomSheet()
//                }
//            }
//
//
//
//            stopWorkLayout.btnStop.setOnClickListener {
//                if (::getVisitDetailsResponse.isInitialized) {
//                    commissioningViewModel.startAndEndWork(
//                        getVisitDetailsResponse.id,
//                        null,
//                        TimePickerUtils.getCurrentTime()
//                    )
//                }
//            }
//
//            startWorkLayout.btnStartWork.setOnClickListener {
//                if (startWorkLayout.btnStartWork.text == getString(R.string.enter_visit_input)) {
//                    navigateToVisitFormFragment()
//                } else {
//                    val hashMap = sharedPrefs.workStatusHashMap
//                    if (::getVisitDetailsResponse.isInitialized) {
//                        commissioningViewModel.startAndEndWork(
//                            getVisitDetailsResponse.id,
//                            TimePickerUtils.getCurrentTime(),
//                            null
//                        )
//                    }
//                }
//            }
//
//
//            stopWorkLayout.btnSubmitCheckSheet.setOnClickListener {
//                if (::getVisitDetailsResponse.isInitialized) {
//                    val action =
//                        CurrentVisitFragmentDirections.actionCurrentVisitFragmentToSubmitCheckSheetFragment(
//                            getVisitDetailsResponse.serviceRequestDetails.id
//                        )
//                    findNavController().navigate(action)
//                }
//            }
//
//
//            startJourneyLayout.btnStartJourney.setOnClickListener {
//                if ((requireActivity() as MainActivity).checkPermissions()) {
//                    if (::getVisitDetailsResponse.isInitialized) {
//                        commissioningViewModel.startJourney(
//                            getVisitDetailsResponse.id,
//                            Date().toFormattedTime()
//                        )
//                    }
//                } else {
//                    (requireActivity() as MainActivity).requestPermissions()
//                }
//            }
//
//
//
//            goCaseLayout.btnGoToCase.setOnClickListener {
//                if (::getVisitDetailsResponse.isInitialized) {
//                    val action =
//                        CurrentVisitFragmentDirections.actionCurrentVisitFragmentToViewOpenCasesFragment(
//                            getVisitDetailsResponse.serviceRequestDetails.id,
//                            getVisitDetailsResponse.serviceRequestDetails.caseId
//                        )
//                    findNavController().navigate(action)
//                }
//            }
//
//
//
//
//            stopJourneyLayout.btnStopJourney.setOnClickListener {
//                getVisitDetailsResponse.latestJourneyDetails?.let {
//                    commissioningViewModel.endJourney(
//                        getVisitDetailsResponse.id,
//                        Date().toFormattedTime(),
//                        it.id
//                    )
//                }
//            }
//
//
//            stopWorkLayout.btnEnterVisitInput.setOnClickListener {
//                navigateToVisitFormFragment()
//            }
//
//
//        }
//    }
//
//
//    fun showJourneyStartedDialog(latitude: String?, longitude: String?) {
//        ViewKotlinUtils.navigationAlertDialog(
//            requireContext(),
//            positiveButtonClickListener = {
//            },
//            negativeButtonClickListener = {
//                ViewUtils.openMapIntent(requireActivity(), latitude, longitude)
//            },
//            title = "Journey Started",
//            subHeading = "Your journey has started successfully. If you would like to navigate to the case’s location, tap “Navigate",
//            positiveBtnName = "Okay",
//            negativeBtnName = "Navigate"
//        )
//    }
//
//
//    fun showConfirmationDialog() {
//        ViewKotlinUtils.navigationAlertDialog(
//            requireContext(),
//            positiveButtonClickListener = {
//                commissioningViewModel.changeVisitStatus(
//                    args.caseId,
//                    "Reached Customer Location"
//                )
//            },
//            negativeButtonClickListener = {
//
//            },
//            title = "Reached Customer Location",
//            subHeading = "Are you sure you want to proceed? By clicking \"yes,\" you won't be able to create any more",
//            positiveBtnName = "Yes",
//            negativeBtnName = "No",
//            true
//        )
//    }
//
//
//    private fun updateVisibilityBasedOnWorkState(isWorkStarted: Boolean) {
//        if (isWorkStarted) {
//            binding.timeStatusLayout.stopWorkLayout.root.isVisible = true
//        } else {
//            binding.timeStatusLayout.startWorkLayout.root.isVisible = true
//        }
//
////        with(binding.timeStatusLayout) {
////            btnStop.visibility = if (isWorkStarted) View.VISIBLE else View.GONE
////            btnObservation.visibility = if (isWorkStarted) View.VISIBLE else View.GONE
////            btnSubmitCheckSheet.visibility = if (isWorkStarted) View.VISIBLE else View.GONE
////            btnStartWork.text = if (isWorkStarted) getString(R.string.enter_visit_input) else getString(R.string.start_work)
////            btnStartJourney.visibility = if (isWorkStarted) View.GONE else View.VISIBLE
////        }
//    }
//
//
//    private fun showCompleteStatus(isCompleted: Boolean) {
//        binding.timeStatusLayout.totalWorkTimeValue.visibility =
//            if (isCompleted) View.GONE else View.VISIBLE
//        binding.timeStatusLayout.totalWorkTimeTitle.visibility =
//            if (isCompleted) View.GONE else View.VISIBLE
//        if (isCompleted) {
//            binding.timeStatusLayout.goCaseLayout.root.isVisible = true
//        }
//    }
//
//    private fun updateUiAccordingToJourney(isJourneyStarted: Boolean) {
//        if (isJourneyStarted) {
//            binding.timeStatusLayout.stopJourneyLayout.root.isVisible = true
//            binding.timeStatusLayout.startJourneyLayout.root.isVisible = false
//            binding.timeStatusLayout.totalWorkTimeTitle.isVisible = true
//            binding.timeStatusLayout.totalWorkTimeValue.isVisible = true
//        } else {
//            binding.timeStatusLayout.stopJourneyLayout.root.isVisible = false
//            binding.timeStatusLayout.startJourneyLayout.root.isVisible = true
//            if (getVisitDetailsResponse.status == "Scheduled") {
//                binding.timeStatusLayout.startJourneyLayout.btnStarttReachedLocation.isVisible =
//                    false
//            }
//            binding.timeStatusLayout.totalWorkTimeTitle.isVisible = false
//            binding.timeStatusLayout.totalWorkTimeValue.isVisible = false
//
//        }
////        with(binding.timeStatusLayout) {
////            btnStopJourney.visibility = if (isJourneyStarted) View.VISIBLE else View.GONE
////            btnStartJourney.visibility = if (isJourneyStarted) View.GONE else View.VISIBLE
////            btnStartWork.visibility = if (isJourneyStarted) View.GONE else View.VISIBLE
//////            btnObservation.visibility = if (isJourneyStarted) View.GONE else View.VISIBLE
//////            btnSubmitCheckSheet.visibility = if (isJourneyStarted)  View.GONE else View.VISIBLE
////        }
//    }
//
//    private fun showObservationBottomSheet() {
//        val observationBottomsheet = ObservationBottomSheet { isSuccess ->
//            if (isSuccess) {
//                binding.timeStatusLayout.stopWorkLayout.btnObservation.text =
//                    getString(R.string.view_observation)
//            }
//        }
//        val bundle = Bundle()
//        bundle.putString(
//            ObservationBottomSheet.COMMISSIONING_VISIT_ID,
//            getVisitDetailsResponse.id
//        )
//        observationBottomsheet.arguments = bundle
//        observationBottomsheet.show(childFragmentManager, "Sheet")
//    }
//
//    private fun navigateToVisitFormFragment() {
//        if (::getVisitDetailsResponse.isInitialized) {
//            val action =
//                CurrentVisitFragmentDirections.actionCurrentVisitFragmentToVisitFormFragment(
//                    getVisitDetailsResponse.id
//                )
//            findNavController().navigate(action)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun show(show: Boolean) {
//        binding.progressBar.isVisible = show
//        binding.instructionLayout.root.isVisible = !show
//        binding.timeLayout.root.isVisible = !show
//        binding.view.isVisible = !show
//        binding.view1.isVisible = !show
//        binding.visitLocationLayout.root.isVisible = !show
//        binding.timeStatusLayout.root.isVisible = !show
//
//
//    }
//
//}
//
//
//
//
//
//
