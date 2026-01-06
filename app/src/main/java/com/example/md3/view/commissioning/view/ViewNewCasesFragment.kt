package com.example.md3.view.commissioning.view

import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings
import com.example.md3.R
import com.example.md3.data.model.commissioning.Details.CommissioningDetailsResponse
import com.example.md3.databinding.FragmentViewCasesBinding
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.Progressive
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.ViewKotlinUtils
import com.example.md3.utils.ViewKotlinUtils.showSnackbarWithAction
import com.example.md3.utils.ViewUtils
import com.example.md3.view.breakdown.view.ProductDetailsBottomSheet
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class ViewNewCasesFragment : Fragment(), Progressive {

    private var _binding: FragmentViewCasesBinding? = null
    private val binding get() = _binding!!
    private val args: ViewNewCasesFragmentArgs by navArgs()
    private val commissioningViewModel: CommissioningViewModel by inject()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var commissioningDetailsResponse: CommissioningDetailsResponse


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewCasesBinding.inflate(inflater, container, false)
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


        commissioningViewModel.caseRequestLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        sharedViewModel.isRefreshRequiredNewCases.postValue(true)
                        binding.btnLayout.root.isVisible = false
                        it.data?.let { caseRequestResponse ->
                            if (caseRequestResponse.status == ConstantStrings.accepted) {
                                showSnackbarWithAction(
                                    binding.btnLayout.root,
                                    getString(R.string.commissioning_case_accepted),
                                    "Go Back"
                                ) {
                                    findNavController().popBackStack()
                                }
                            } else if (caseRequestResponse.status == ConstantStrings.rejected) {
                                showSnackbarWithAction(
                                    binding.btnLayout.root,
                                    getString(R.string.commissioning_case_rejected),
                                    ""
                                ) {}
                                binding.tvCaseRejected.isVisible = true
                            }
                        }
                    }

                    Status.ERROR -> {
                        binding.btnLayout.root.isVisible = false
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }

    }


    private fun updateUI() {
        binding.apply {
            binding.tabLayout.isVisible = false
            binding.viewPager.isVisible = false


            warrantyInfoLayout.root.isVisible = false


            if (commissioningDetailsResponse.status == ConstantStrings.rejected) {
                binding.tvCaseRejected.isVisible = true
            } else {
                binding.btnLayout.root.isVisible = true
            }

            binding.caseStateLayout.tvStatus.setChipBackgroundColorResource(
                KotlinFunctions.getCaseStatusColor(
                    commissioningDetailsResponse.status
                )
            )
            binding.caseStateLayout.tvStatus.text = commissioningDetailsResponse.status
            if(commissioningDetailsResponse.type == "Break Down"){
                binding.caseStateLayout.tvcomType.text = "Breakdown"
                binding.caseStateLayout.imageView2.setImageResource(R.drawable.breakdown)
            }else{
                binding.caseStateLayout.tvcomType.text = "Commissioning"
                binding.caseStateLayout.imageView2.setImageResource(R.drawable.commissioninglogo)
            }



            binding.productInfoLayout.root.setOnClickListener {
                val productDetailsBottomSheet = ProductDetailsBottomSheet(commissioningDetailsResponse.product)
                productDetailsBottomSheet.show(childFragmentManager,"")
            }



            binding.caseStateLayout.tvPlannedDate.text = commissioningDetailsResponse.plannedDate
            binding.caseStateLayout.tvIssue.text = commissioningDetailsResponse.description
            binding.customerInfoLayout.tvCustomerName.text =
                commissioningDetailsResponse.customerDetails.name

//            binding.productInfoLayout.tvProductName.text =
//                commissioningDetailsResponse.product.productName
//            binding.productInfoLayout.tvName.text = commissioningDetailsResponse.product.productName
            binding.productInfoLayout.tvProductName.text = commissioningDetailsResponse.productName
            binding.productInfoLayout.tvName.text = commissioningDetailsResponse.productName

        }

    }

    private fun apiCall() {
        commissioningViewModel.getCommissioningDetails(args.caseId)
    }

    private fun clickableViews() {
        binding.toolbarLayout.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.customerInfoLayout.ivCall.setOnClickListener { view ->
            if (::commissioningDetailsResponse.isInitialized) {
                ViewUtils.phoneIntent(
                    commissioningDetailsResponse.customerDetails.phone,
                    requireActivity()
                )
            }
        }

        binding.customerInfoLayout.ivNavigate.setOnClickListener {
            if (::commissioningDetailsResponse.isInitialized) {
                ViewUtils.openMapIntent(
                    requireActivity(),
                    commissioningDetailsResponse.customerAddress.latitude,
                    commissioningDetailsResponse.customerAddress.longitude
                )
            }
        }


        binding.btnLayout.btnAccept.setOnClickListener {
            if (::commissioningDetailsResponse.isInitialized) {
                showAssignmentDialogBox(commissioningEngineerId = args.commEngId)
            }
        }


        binding.btnLayout.btnReject.setOnClickListener {
            if (::commissioningDetailsResponse.isInitialized) {
                showRejectDialogBox(commissioningEngineerId = args.commEngId)
            }
        }


    }

    private fun initView() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
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


    private fun initToolbar() {
        binding.toolbarLayout.topAppBar.title = args.formattedCaseId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(show: Boolean) {
        binding.progressBar.isVisible = show
        binding.container.isVisible = !show
    }
}
