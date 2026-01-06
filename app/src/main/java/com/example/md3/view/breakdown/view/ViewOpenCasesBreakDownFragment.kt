package com.example.md3.view.breakdown.view

import Status
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings
import com.example.md3.R
import com.example.md3.data.model.commissioning.Details.CommissioningDetailsResponse
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.databinding.FragmentViewCasesBreakdownBinding
import com.example.md3.utils.GenericViewPagerAdapter
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.Progressive
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.commissioning.view.ViewOpenCasesFragmentDirections
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

class ViewOpenCasesBreakDownFragment : Fragment() , Progressive {

    private var _binding: FragmentViewCasesBreakdownBinding? = null
    private val binding get() = _binding!!
    private val args: ViewOpenCasesBreakDownFragmentArgs by navArgs()
    private val commissioningViewModel: CommissioningViewModel by inject()
    private lateinit var viewPagerAdapter: GenericViewPagerAdapter<Fragment>
    private lateinit var getVisitDetailsResponse: GetVisitDetailsResponse
    private lateinit var commissioningDetailsResponse: CommissioningDetailsResponse
    private var commissioningStatus : Boolean = false

    private var isWorkStarted = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewCasesBreakdownBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        apiCall()
        initToolbar()
        clickableViews()
        observe()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observe() {



        commissioningViewModel.mrnDetailsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            binding.addedMrnLayout.root.isVisible = true
                            binding.viewbelow.isVisible = true
                        }
                    }

                    Status.ERROR -> {
                        binding.addedMrnLayout.root.isVisible = false
                        binding.viewbelow.isVisible = false
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }

        commissioningViewModel.getCommissioningStatus.observe(viewLifecycleOwner){
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        commissioningStatus = true
                    }

                    Status.ERROR -> {
                        commissioningStatus = false
                    }

                    Status.LOADING -> {
                    }
                }
            }
        }

        commissioningViewModel.getCommissioningDetailsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        it.data?.let { data ->
                            commissioningDetailsResponse = data
                            updateUI()
                        }
                    }

                    Status.ERROR -> {
                        show(false)

                    }

                    Status.LOADING -> {
                        show(true)
                    }
                }
            }

        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("CONCLUSION_STATUS")
            ?.observe(viewLifecycleOwner) { result ->
                if (result == "success") {
                    if (::getVisitDetailsResponse.isInitialized) {
                        commissioningViewModel.getCommissioningDetails(args.caseId)
                    }
                }
            }

//        commissioningViewModel.getVisitDetailsMutableLiveData.observe(viewLifecycleOwner) {
//            if (!it.isResponseHandled()) {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        show(false)
//                        it.data?.let { details ->
//                            getVisitDetailsResponse = details
//                        }
//                    }
//
//                    Status.ERROR -> {
//                        show(false)
//                        ViewUtils.showSnackbar(binding.toolbarLayout.root,it.message)
//                    }
//
//                    Status.LOADING -> {
//                        show(true)
//                    }
//                }
//            }
//
//        }
    }


//    private fun updateUITest() {
//        binding.caseStateLayout.tvStatus.text = getVisitDetailsResponse.status
//        binding.caseStateLayout.tvPlannedDate.text = "Planned Date:" + getVisitDetailsResponse.serviceRequestDetails.plannedDate
//        binding.caseStateLayout.tvIssue.text =
//            getVisitDetailsResponse.serviceRequestDetails.description
//        binding.customerInfoLayout.tvCustomerName.text =
//            getVisitDetailsResponse.serviceRequestDetails.customerDetails.name
//        binding.productInfoLayout.tvProductName.text =
//            getVisitDetailsResponse.serviceRequestDetails.product
//        binding.productInfoLayout.tvName.text =
//            getVisitDetailsResponse.serviceRequestDetails.product
//    }


    private fun updateUI() {
        binding.apply {
            binding.caseStateLayout.tvStatus.setChipBackgroundColorResource(KotlinFunctions.getCaseStatusColor(commissioningDetailsResponse.status))
            binding.caseStateLayout.tvStatus.setChipBackgroundColorResource(KotlinFunctions.getCaseStatusColor(commissioningDetailsResponse.status))
            binding.caseStateLayout.tvStatus.text = commissioningDetailsResponse.status
            binding.caseStateLayout.tvPlannedDate.text = commissioningDetailsResponse.plannedDate
            binding.caseStateLayout.tvIssue.text = commissioningDetailsResponse.description
            binding.customerInfoLayout.tvCustomerName.text = commissioningDetailsResponse.customerDetails.name
//            binding.productInfoLayout.tvProductName.text = commissioningDetailsResponse.product.productName
//            binding.productInfoLayout.tvName.text = commissioningDetailsResponse.product.productName
            binding.productInfoLayout.tvProductName.text = commissioningDetailsResponse.productName
            binding.productInfoLayout.tvName.text = commissioningDetailsResponse.productName
            setAdapter(commissioningDetailsResponse.id)

            when (commissioningDetailsResponse.status){
                "In-Progress" -> {
                    binding.caseStateLayout.btnConcludeCase.isVisible =  true
                }
                "Closed" -> {
                    binding.caseStateLayout.btnConcludeCase.isVisible =  true
                    binding.caseStateLayout.btnConcludeCase.setTextColor(requireContext().getColor(R.color.md_theme_primary))
                    binding.caseStateLayout.btnConcludeCase.strokeColor = ColorStateList.valueOf(getResources().getColor(R.color.md_theme_outline))
                    binding.caseStateLayout.btnConcludeCase.text = "View Conclusion Details"
                }
            }

            commissioningViewModel.getCommissioningStatus(args.caseId)

        }

    }

    private fun apiCall() {
        commissioningViewModel.getCommissioningDetails(args.caseId)
        commissioningViewModel.getAllMrnDetails(args.caseId, "")
    }

    private fun clickableViews() {
        binding.toolbarLayout.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.productInfoLayout.root.setOnClickListener {
            val productDetailsBottomSheet = ProductDetailsBottomSheet(commissioningDetailsResponse.product)
            productDetailsBottomSheet.show(childFragmentManager,"")
        }

        binding.addedMrnLayout.root.setOnClickListener {
            val showMRNPartsBottomSheet = ShowMRNPartsBottomSheet(args.caseId)
            showMRNPartsBottomSheet.show(childFragmentManager,"")
        }



        binding.caseStateLayout.btnConcludeCase.setOnClickListener {
            println(" status ${commissioningDetailsResponse.status}")
            when (commissioningDetailsResponse.status){
                "In-Progress" -> {
                    if(commissioningDetailsResponse.flagAllVisitCompleted && commissioningStatus){
                        val action  = ViewOpenCasesBreakDownFragmentDirections.actionViewOpenCasesBreakDownFragmentToConclusionFormFragment(args.caseId)
                        findNavController().navigate(action)
                    }else{
                        ViewUtils.showSnackbar(binding.root,"All the checkSheet must be filled before going to conclusion.")
                    }
                }
                "Closed" -> {
                    val bundle = Bundle()
                    bundle.putString(ConclusionDetailsBottomSheet.ORG_COMMISSIONING_ID, args.caseId)
                    val bottomSheet = ConclusionDetailsBottomSheet()
                    bottomSheet.arguments = bundle
                    bottomSheet.show(childFragmentManager, "")
                }
            }
        }




        binding.customerInfoLayout.ivCall.setOnClickListener { view ->
            if (::commissioningDetailsResponse.isInitialized) {
                ViewUtils.phoneIntent(
                    commissioningDetailsResponse.customerDetails.phone,
                    requireActivity()
                )
            }
        }


        binding.customerInfoLayout.ivNavigate.setOnClickListener { view ->
            if (::commissioningDetailsResponse.isInitialized) {
                ViewUtils.openMapIntent(requireActivity(), commissioningDetailsResponse.customerAddress.latitude, commissioningDetailsResponse.customerAddress.longitude)
            }
        }


        binding.warrantyInfoLayout.root.setOnClickListener {
            val action = ViewOpenCasesBreakDownFragmentDirections.actionViewOpenCasesBreakDownFragmentToWarrantyAndOtherDetailsFragment(args.caseId,true)
            findNavController().navigate(action)
        }




    }

    private fun initView() {
        binding.caseStateLayout.tvcomType.text = "BreakDown"
    }


    fun setAdapter(id: String) {
        val fragmentTitles = listOf("My Visits", "All Visits")
        val fragmentClasses = listOf(
            PastVisitBreakDownFragment::class.java,
            PastVisitBreakDownFragment::class.java
        )

        viewPagerAdapter = GenericViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            fragmentTitles,
            fragmentClasses,
            ConstantStrings.VISIT_TAB_TYPES,
            id
        )

        with(binding.viewPager) {
            adapter = viewPagerAdapter
            offscreenPageLimit = androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()

    }


    private fun initToolbar() {
        binding.toolbarLayout.topAppBar.title = args.formattedCaseId
        binding.toolbarLayout.topAppBar.inflateMenu(R.menu.mrn_options)
        binding.toolbarLayout.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.createMrn -> {
                    val action = ViewOpenCasesBreakDownFragmentDirections.actionViewOpenCasesBreakDownFragmentToCreateMRNFragment(isFromBreakdown = true , serviceRequestId = args.caseId)
                    findNavController().navigate(action)
                    true // Return true to consume the event
                }
                R.id.rootAnalysis -> {
                    val action = ViewOpenCasesFragmentDirections.actionGlobalRootAnalysisFragment()
                    findNavController().navigate(action)
                    true // Return true to consume the event
                }
                R.id.claimPart -> {
                    val action = ViewOpenCasesBreakDownFragmentDirections.actionGlobalClaimPartFragment(args.caseId,true)
                    findNavController().navigate(action)
                    true // Return true to consume the event
                }
//                R.id.spareRequest -> {
//
//                    true // Return true to consume the event
//                }
                else -> false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(show: Boolean) {
        binding.progressBar.isVisible =  show
        binding.container.isVisible = !show
    }
}
