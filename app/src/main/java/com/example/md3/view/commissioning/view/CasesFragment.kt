package com.example.md3.view.commissioning.view

import Status
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.data.model.commissioning.JourneyVisitRoute.JourneyVisitRouteResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.Route
import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.example.md3.data.model.commissioning.new_case.NewCasesResult
import com.example.md3.data.model.commissioning.visit.VisitResult
import com.example.md3.data.model.visit.CreateVisitBottomSheet
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.AllCasesLayoutBinding
import com.example.md3.databinding.FragmentNewCaseBinding
import com.example.md3.databinding.NewCaseItemLayoutBinding
import com.example.md3.databinding.OpenCaseItemLayoutBinding
import com.example.md3.utils.GenericPagingAdapter
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.TabType
import com.example.md3.utils.ViewKotlinUtils
import com.example.md3.utils.ViewKotlinUtils.toFormattedTime
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.commissioning.adapter.CaseInfoAdapter
import com.example.md3.view.commissioning.adapter.ItemModel
import com.example.md3.view.home.view.HomeFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Date


class CasesFragment : Fragment(R.layout.fragment_new_case) {

    private var journeyVisitId: String? = null
    private val sharedPrefs: SharedPrefs by inject()
    private var latitude: String? = null
    private var longitude: String? = null
    private var selectedVisitCount : String = ""
    var latLongList: List<Route>? = emptyList()
    private var _binding: FragmentNewCaseBinding? = null
    private val commissioningViewModel: CommissioningViewModel by inject()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var newCasePagingAdapter: GenericPagingAdapter<NewCasesResult>
    private lateinit var upcomingVisitAdapter: GenericPagingAdapter<VisitResult>
    private lateinit var allCasesAdapter: GenericPagingAdapter<ServiceRequestDetails>
    private val binding get() = _binding!!
    private lateinit var tabType: TabType

    private  val TAG = "CasesFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabType = it.getSerializable(ARG_TAB_TYPE) as TabType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewCaseBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initRecyclerview()
        initAdapters()
        observe()
        clickableViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun clickableViews() {
        binding.apply {
            recyclerviewLayout.swipRefreshLayout.setOnRefreshListener {
                when (tabType) {
                    TabType.NEW -> {
                        newCasePagingAdapter.refresh()
                    }

                    TabType.UPCOMING_VISITS -> {
                        upcomingVisitAdapter.refresh()
                    }

                    TabType.ALL_CASES -> {
                        allCasesAdapter.refresh()
                    }

                    else -> {

                    }
                }
                recyclerviewLayout.swipRefreshLayout.isRefreshing = false
            }

            recyclerviewLayout.buttonRetry.setOnClickListener {
                when (tabType) {
                    TabType.NEW -> {
                        newCasePagingAdapter.retry()
                    }

                    TabType.UPCOMING_VISITS -> {
                        newCasePagingAdapter.retry()
                    }

                    TabType.ALL_CASES -> {
                        newCasePagingAdapter.retry()
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun initAdapters() {
        newCasePagingAdapter = GenericPagingAdapter(
            R.layout.new_case_item_layout,
            { view, item , position ->
                val binding = NewCaseItemLayoutBinding.bind(view)
                binding.tvComCaseNo.text = item.serviceRequestDetails.caseId ?: ""
                binding.customNameValue.text = item.serviceRequestDetails.customerDetails.name
                binding.callPlanDateValue.text = item.serviceRequestDetails.plannedDate
                binding.tvStatus.text = item.status

                binding.tvStatus.setChipBackgroundColorResource(
                    KotlinFunctions.getCaseStatusColor(
                        item.status
                    )
                )


                binding.btnAccept.isVisible = item.status == "Pending"
                binding.btnReject.isVisible = item.status == "Pending"


                binding.btnAccept.setOnClickListener {
                    showAssignmentDialogBox(commissioningEngineerId = item.id)
                }
                binding.btnReject.setOnClickListener {
                    showRejectDialogBox(commissioningEngineerId = item.id)
                }

                Log.d(TAG, "initAdapters: " + sharedPrefs.organisationId)

                binding.root.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToViewNewCasesFragment(
                        item.serviceRequestDetails.id,
                        item.serviceRequestDetails.caseId,
                        item.id
                    )
                    findNavController().navigate(action)
                }


            },
            { item ->

            }
        )

        newCasePagingAdapter.addLoadStateListener { loadStates ->

            val refreshState = loadStates.source.refresh
            binding.recyclerviewLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading

            when (refreshState) {
                is LoadState.Loading -> {
                    binding.recyclerviewLayout.progressBar.isVisible = true
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
                }

                is LoadState.NotLoading -> {
                    if (newCasePagingAdapter.itemCount == 0) {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = false
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                        binding.recyclerviewLayout.tvEmptyTitle.text =
                            getString(R.string.no_new_cases_found)
                    } else {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = true
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
                        binding.recyclerviewLayout.buttonRetry.isVisible = false
                    }
                }

                is LoadState.Error -> {
                    binding.recyclerviewLayout.progressBar.isVisible = false
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                    binding.recyclerviewLayout.buttonRetry.isVisible = true
                    binding.recyclerviewLayout.tvEmptyTitle.text =
                        getString(R.string.something_went_wrong)
                }

            }
        }















        upcomingVisitAdapter = GenericPagingAdapter(
            R.layout.open_case_item_layout,
            { view, openCasesData , position ->
                val binding = OpenCaseItemLayoutBinding.bind(view)
                binding.customerNameTitle.text = openCasesData.servicerequestdetails.customerDetails.name
                binding.customerNameValue.text = openCasesData.servicerequestdetails.caseId
                binding.callPlanDateValue.text = openCasesData.servicerequestdetails.plannedDate
                binding.totalVisitsValue.text = openCasesData.servicerequestdetails.customerDetails.name
                binding.customerRatingValue.text =
                    if (openCasesData.isNextVisitRequired) "Yes, ${openCasesData.nextVisitDate}" else "No"
                binding.tvStatus.text = openCasesData.status
                binding.tvStatus.setChipBackgroundColorResource(
                    KotlinFunctions.getCaseStatusColor(
                        openCasesData.status
                    )
                )

                if (openCasesData.status == "Journey Started") {
                    binding.journeyStartedGroup.isVisible = true
                    binding.journeyStartGroup.isVisible = false
                    binding.btnLargeViewCase.isVisible = false
                } else if(openCasesData.status == "Work Started"){
                    binding.btnLargeViewCase.isVisible = true
                    binding.journeyStartedGroup.isVisible = false
                    binding.journeyStartGroup.isVisible = false
                } else if (openCasesData.status == "Reached Customer Location"){
                    binding.tvStatus.text = "Reached Location"
                    binding.btnLargeViewCase.isVisible = true
                    binding.journeyStartedGroup.isVisible = false
                    binding.journeyStartGroup.isVisible = false
                }else{
                    binding.btnLargeViewCase.isVisible = false
                    binding.journeyStartGroup.isVisible = true
                    binding.journeyStartedGroup.isVisible = false
                }



                binding.btnStartJourney.setOnClickListener {

                    selectedVisitCount = "Visit # " + openCasesData.visitCount.toString()

                    lifecycleScope.launch {
                        binding.progressBar.isVisible =  true
                        binding.btnStartJourney.text = ""
                        binding.btnStartJourney.icon = null
                        if ((requireActivity() as MainActivity).startGettingLocation()) {
                            latLongList = null
                            while (latLongList.isNullOrEmpty()) {
                                latLongList = (requireActivity() as MainActivity).getUpdatedLatAndLong()
                                delay(1000)
                            }
                            if (!latLongList.isNullOrEmpty()) {
                                commissioningViewModel.startJourney(
                                    openCasesData.id,
                                    Date().toFormattedTime()
                                )
                                binding.progressBar.isVisible =  false
                                binding.btnStartJourney.text = getString(R.string.start_journey)
                                binding.btnStartJourney.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_start)
                            }
                        } else {
                            (requireActivity() as MainActivity).startGettingLocation()
                        }
                    }



                }




                binding.btnStop.setOnClickListener {

                    val journeyId = openCasesData.latestJourneyDetails?.id ?: ""
                    val caseId  = openCasesData.id ?: ""

                    val list = (requireActivity() as MainActivity).getUpdatedLatAndLong()
                    commissioningViewModel.journeyVisitEndRoute(
                        caseId,
                        JourneyVisitRouteResponse(
                            journeyId,
                            route = list
                        )
                    )

                }



                binding.btnNavigate.setOnClickListener {
                    ViewUtils.openMapIntent(
                        requireActivity(),
                        openCasesData.visitAddress.latitude,
                        openCasesData.visitAddress.longitude
                    )
                }

                binding.root.setOnClickListener {
                    binding.btnViewCase.performClick()
                }


                binding.btnViewCase.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToCurrentVisitFragment(
                        openCasesData.id,
                        "Visit # " + openCasesData.visitCount.toString()
                    )
                    findNavController().navigate(action)
                }

                binding.btnLargeViewCase.setOnClickListener {
                    binding.btnViewCase.performClick()
                }
            },
            { item ->

            }
        )



        upcomingVisitAdapter.addLoadStateListener { loadStates ->

            val refreshState = loadStates.source.refresh
            binding.recyclerviewLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading

            when (refreshState) {
                is LoadState.Loading -> {
                    binding.recyclerviewLayout.progressBar.isVisible = true
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
                }

                is LoadState.NotLoading -> {
                    if (upcomingVisitAdapter.itemCount == 0) {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = false
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                        binding.recyclerviewLayout.tvEmptyTitle.text =
                            getString(R.string.no_open_cases_found)
                    } else {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = true
                        binding.recyclerviewLayout.buttonRetry.isVisible = false
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
                    }
                }

                is LoadState.Error -> {
                    binding.recyclerviewLayout.progressBar.isVisible = false
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                    binding.recyclerviewLayout.buttonRetry.isVisible = true
                    binding.recyclerviewLayout.tvEmptyTitle.text =
                        getString(R.string.something_went_wrong)
                }

            }
        }



        allCasesAdapter = GenericPagingAdapter(
            R.layout.all_cases_layout,
            { view, closedCasesResult , position ->
                val binding = AllCasesLayoutBinding.bind(view)
                binding.customerNameTitle.text = closedCasesResult.customerDetails.name
                binding.customerNameValue.text = closedCasesResult.caseId
                binding.tvStatus.text = closedCasesResult.status
                binding.tvStatus.setChipBackgroundColorResource(
                    KotlinFunctions.getCaseStatusColor(
                        closedCasesResult.status
                    )
                )
                val caseInfoAdapter = CaseInfoAdapter{
                    binding.btnViewCase.performClick()
                }

                binding.recyclerview.adapter = caseInfoAdapter


                binding.root.setOnClickListener {
                    binding.btnViewCase.performClick()
                }

                if (closedCasesResult.status == "Closed") {

                    val list = listOf<ItemModel>(
                        ItemModel("Total Work Time", "---------"),
                        ItemModel("Total Journey Time", "---------"),
                        ItemModel("Total Visit", "--------"),
                        ItemModel(
                            "Customer Name",
                            closedCasesResult.customerDetails.name ?: "-------"
                        ),
                    )
                    caseInfoAdapter.submitList(list)
                    binding.customerNameValue.setTextColor(resources.getColor(R.color.colorSuccess))
                    binding.tvStatus.isVisible = false
                    binding.commissioningTitle2.isVisible = true
                    binding.caseIdValue.isVisible = true
                    binding.commissioningTitle.visibility = View.INVISIBLE
                    binding.btnLargeViewCase.isVisible = true
                    binding.btnViewCase.visibility = View.GONE
                    binding.btnCreateVisit.visibility = View.GONE
                } else {

                    binding.customerNameValue.setTextColor(resources.getColor(R.color.md_theme_onSurface))
                    val list = listOf<ItemModel>(
                        ItemModel("Call Plan Date", closedCasesResult.plannedDate),
                        ItemModel("Supplier", "---------"),
                        ItemModel("Customer Name", closedCasesResult.customerDetails.name),
                    )
                    caseInfoAdapter.submitList(list)
                    binding.tvStatus.isVisible = true
                    binding.commissioningTitle2.isVisible = false
                    binding.caseIdValue.isVisible = false
                    binding.commissioningTitle.visibility = View.VISIBLE
                    binding.btnLargeViewCase.isVisible = false


                }


                binding.groupCreateVisit.isVisible = closedCasesResult.status == "Open" || closedCasesResult.status == "In-Progress"




                binding.btnLargeViewCase.setOnClickListener {
                    binding.btnViewCase.performClick()
                }


                binding.root.setOnClickListener {
                    binding.btnViewCase.performClick()
                }

                binding.btnCreateVisit.setOnClickListener {
                    val createVisitBottomSheet = CreateVisitBottomSheet(false,closedCasesResult) {
                        ViewUtils.showSnackbar(
                            binding.recyclerview.rootView,
                            "Visit Created Successfully.."
                        )
                    }
                    createVisitBottomSheet.show(childFragmentManager, "")
                }


                binding.btnViewCase.setOnClickListener {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToViewOpenCasesFragment(
                                closedCasesResult.id,
                                closedCasesResult.caseId
                            )
                        findNavController().navigate(action)

                }


            },
            { item ->

            }
        )

        allCasesAdapter.addLoadStateListener { loadStates ->

            val refreshState = loadStates.source.refresh
            binding.recyclerviewLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading

            when (refreshState) {
                is LoadState.Loading -> {
                    binding.recyclerviewLayout.progressBar.isVisible = true
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
                }

                is LoadState.NotLoading -> {
                    if (allCasesAdapter.itemCount == 0) {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = false
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                        binding.recyclerviewLayout.tvEmptyTitle.text =
                            getString(R.string.no_closed_cases_found)
                    } else {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = true
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
                        binding.recyclerviewLayout.buttonRetry.isVisible = false
                    }
                }

                is LoadState.Error -> {
                    binding.recyclerviewLayout.progressBar.isVisible = false
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                    binding.recyclerviewLayout.buttonRetry.isVisible = true
                    binding.recyclerviewLayout.tvEmptyTitle.text =
                        getString(R.string.something_went_wrong)
                }

            }
        }

    }






    private fun initRecyclerview() {
        binding.recyclerviewLayout.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initViews() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    private fun initRv() {
        binding.recyclerviewLayout.apply {
            newCasePagingAdapter = GenericPagingAdapter(
                R.layout.new_case_item_layout,
                { view, item , position ->
                    val binding = NewCaseItemLayoutBinding.bind(view)
                    binding.tvComCaseNo.text = item.serviceRequestDetails.caseId
                    binding.customNameValue.text = item.serviceRequestDetails.customerDetails.name
                    binding.callPlanDateValue.text = item.serviceRequestDetails.plannedDate
                    binding.btnAccept.setOnClickListener {
                        showAssignmentDialogBox(commissioningEngineerId = item.id)
                    }
                    binding.btnReject.setOnClickListener {
                        showRejectDialogBox(commissioningEngineerId = item.id)
                    }
                },
                { item ->

                }
            )
            recyclerView.adapter = newCasePagingAdapter

            recyclerView.layoutManager = LinearLayoutManager(context)

            newCasePagingAdapter.addLoadStateListener { combinedLoadStates ->
                val refreshState = combinedLoadStates.source.refresh

                progressBar.isVisible = refreshState is LoadState.Loading
                buttonRetry.isVisible = refreshState is LoadState.Error
                handleError(combinedLoadStates)



                buttonRetry.setOnClickListener {
                    newCasePagingAdapter.retry()
                }

            }
        }
    }

    private fun showRejectDialogBox(commissioningEngineerId: String) {
        ViewKotlinUtils.rejectAlertDialog(
            requireContext(),
            positiveButtonClickListener = {
                commissioningViewModel.changeCaseRequest(
                    status = ConstantStrings.rejected,
                    reason = it,
                    commissioningEngineerId = commissioningEngineerId
                )
            },
            negativeButtonClickListener = {})
    }


    private fun showAssignmentDialogBox(commissioningEngineerId: String) {
        ViewKotlinUtils.assignmentAlertDialog(
            requireContext(),
            positiveButtonClickListener = {
                commissioningViewModel.changeCaseRequest(
                    status = ConstantStrings.accepted,
                    reason = "",
                    commissioningEngineerId = commissioningEngineerId
                )
            },
            negativeButtonClickListener = {

            }
        )
    }


    private fun handleError(combinedLoadStates: CombinedLoadStates) {
        val errorState = combinedLoadStates.source.append as? LoadState.Error
            ?: combinedLoadStates.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), "${it.error}", Toast.LENGTH_LONG).show()
        }
    }


    private fun observe() {


        commissioningViewModel.journeyVisitEndRouteLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val caseId = it.data?.id ?: ""
                        val journeyId = it.data?.latestJourneyDetails?.id ?: ""

                        commissioningViewModel.endJourney(
                            caseId,
                            Date().toFormattedTime(),
                            journeyId
                        )

                    }

                    Status.ERROR -> {
                        ViewUtils.showSnackbar(binding.root, it.message)
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }


        sharedViewModel.isRefreshRequiredCommissioning.observe(viewLifecycleOwner){ it ->
            if(it == true){
                commissioningViewModel.refreshUpcomingVisit()
                sharedViewModel.isRefreshRequiredCommissioning.postValue(false)
            }
        }


        sharedViewModel.isRefreshRequiredNewCases.observe(viewLifecycleOwner){
            if(it == true){
                commissioningViewModel.refreshNewCases()
                sharedViewModel.isRefreshRequiredNewCases.postValue(false)
            }
        }


        commissioningViewModel.getCreateVisitLiveData.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            val action =
                                HomeFragmentDirections.actionHomeFragmentToCurrentVisitFragment(
                                    it.id,
                                    "Visit # " + it.visitCount.toString()
                                )
                            findNavController().navigate(action)
                        }
                    }

                    Status.ERROR -> {
                        ViewUtils.showSnackbar(binding.recyclerviewLayout.root, it.message)
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }


        when (tabType) {
            TabType.NEW -> {
                binding.recyclerviewLayout.recyclerView.adapter = newCasePagingAdapter
                commissioningViewModel.getAllNewCases.observe(viewLifecycleOwner) {
                    newCasePagingAdapter.submitData(lifecycle, it)
                }
            }

            TabType.UPCOMING_VISITS -> {
                binding.recyclerviewLayout.recyclerView.adapter = upcomingVisitAdapter
                commissioningViewModel.getAllOpenCases.observe(viewLifecycleOwner) {
                    upcomingVisitAdapter.submitData(lifecycle, it)
                }
            }

            TabType.ALL_CASES -> {
                binding.recyclerviewLayout.recyclerView.adapter = allCasesAdapter
                commissioningViewModel.getAllCases.observe(viewLifecycleOwner) {
                    allCasesAdapter.submitData(lifecycle, it)
                }
            }

            else -> {}
        }


        sharedViewModel.getCurrentLocation.observe(viewLifecycleOwner) {
            latitude = it.first
            longitude = it.second
        }


        commissioningViewModel.caseRequestLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        newCasePagingAdapter.refresh()
                        if (it.data?.status == ConstantStrings.accepted) {
                            Snackbar.make(
                                binding.recyclerviewLayout.root,
                                "Commissioning Case Accepted",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Open Case") {
                                    commissioningViewModel.refreshOpenCasesList()
                                    sharedViewModel.setTabData(TabType.ALL_CASES)
                                }
                                .show()
                        }

                    }

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }
                }
            }
        }


        commissioningViewModel.startJourneyLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        commissioningViewModel.journeyVisitRoute(
                            it.data?.commissioningVisit!!,
                            JourneyVisitRouteResponse(
                                it.data.id,
                                route = latLongList ?: mutableListOf()
                            )
                        )
                    }

                    Status.ERROR -> {
                        it.message?.let { it1 ->
                            (requireActivity() as MainActivity).anotherCaseAlertDialog(
                                it1,
                                selectedVisitCount
                            )
                        }
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }


        commissioningViewModel.journeyVisitRouteLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { res ->
                            if (::upcomingVisitAdapter.isInitialized) {
                                upcomingVisitAdapter.refresh()
                            }
                            showJourneyStartedDialog(
                                res.visitAddress.latitude,
                                res.visitAddress.longitude
                            )
                        }
                    }

                    Status.ERROR -> {
                        ViewUtils.showSnackbar(binding.root, it.message)
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }



        commissioningViewModel.endJourneyLiveData.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (::upcomingVisitAdapter.isInitialized) {
                            (requireActivity() as MainActivity).stopGettingLocation()
                            upcomingVisitAdapter.refresh()
                        }
                    }

                    Status.ERROR -> {
                        ViewUtils.showSnackbar(binding.recyclerviewLayout.recyclerView , it.message)
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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


    companion object {
        const val ARG_TAB_TYPE = "arg_tab_type"

        fun newInstance(tabType: TabType): CasesFragment {
            val fragment = CasesFragment()
            val args = Bundle()
            args.putSerializable(ARG_TAB_TYPE, tabType)
            fragment.arguments = args
            return fragment
        }
    }

}