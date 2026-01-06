package com.example.md3.view.visit

import Status
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.databinding.VisitInputDetailsBottomsheetBinding
import com.example.md3.utils.Progressive
import com.example.md3.view.checksheet.CheckSheetBottomSheet
import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.commissioning.observation.view.ViewObservationBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject


class VisitInputDetailsBottomSheet() : BottomSheetDialogFragment(), Progressive {

    private var _binding: VisitInputDetailsBottomsheetBinding? = null
    private val commissioningViewModel: CommissioningViewModel by inject()
    lateinit var getVisitDetailsResponse : GetVisitDetailsResponse
    private val binding get() = _binding!!
    private var visitID: String? = null
    private var isFromCommission : Boolean = true


    companion object {
        const val VISIT_ID = "visit_id"
        const val IS_FROM_COMMISSION = "is_from_commission"
        const val TAG = "YourBottomSheetFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        visitID = arguments?.getString(VISIT_ID)
        isFromCommission = arguments?.getBoolean(IS_FROM_COMMISSION , true) == true

        _binding = VisitInputDetailsBottomsheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSheetBehavior()
        initViews()
        apiCall()
        observe()
        clickableViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        if(!isFromCommission){
            binding.tvCommissionStatusTitle.text = "BreakDown Status"
        }
    }

    private fun clickableViews() {
        binding.apply {
            btnViewObservation.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(ViewObservationBottomSheet.VISIT_ID, visitID)
                val observationBottomSheet = ViewObservationBottomSheet()
                observationBottomSheet.arguments = bundle
                observationBottomSheet.show(childFragmentManager, "")
            }
            btnViewCheckList.setOnClickListener {
                if(::getVisitDetailsResponse.isInitialized){
                    val bundle = Bundle()
                    bundle.putString(CheckSheetBottomSheet.COMMISSIONING_VISIT_ID, getVisitDetailsResponse.serviceRequestDetails.id)
                    val checkSheetBottomSheet = CheckSheetBottomSheet()
                    checkSheetBottomSheet.arguments = bundle
                    checkSheetBottomSheet.show(childFragmentManager, "")
                }
            }
            btnBack.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun observe() {
        commissioningViewModel.getVisitDetailsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        it.data?.let { it1 ->
                            getVisitDetailsResponse = it1
                            setValues(it1)
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
    }

    private fun apiCall() {
        visitID?.let { commissioningViewModel.getVisitDetails(it) }
    }


    private fun setSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    private fun setValues(data: GetVisitDetailsResponse) {
        binding.tvEndVisitDate.text = "End Visit Date  : ${data.endScheduledWorkDate?.toString() ?: "---"}"
        binding.tvEndVisitTime.text = "End Visit Time : ${data.endScheduledWorkTime?.toString() ?: "---"}"
        binding.tvVisitSummeryValue.text = data.summary ?: "---"
        binding.tvExplainStatus.text = data.explainStatus ?: "---"
        binding.tvCommissionStatusValue.text = data.visitCommissioningStatus ?: "---------------"

        val scheduledDate = data.scheduledWorkDate ?: "---"
        val scheduledTime = data.scheduledWorkTime ?: "---"
        binding.tvVisitDetails.text = "Scheduled Date : $scheduledDate \n Scheduled Time : $scheduledTime"

        val isNextVisitRequired = data.isNextVisitRequired ?: "---"
        val nextVisitDate = data.nextVisitDate ?: "---"
        val nextVisitTime = data.nextVisitTime ?: "---"
        binding.tvAnotherVisitValue.text = "Is Another Visit Required: $isNextVisitRequired" +
                "\n Next Visit Scheduled Date: $nextVisitDate \n Next Visit time: $nextVisitTime"

        val engineerName = data.assignedEngineerDetails?.name ?: "---"
        val engineerPhone = data.assignedEngineerDetails?.phone ?: "---"
        val engineerEmail = data.assignedEngineerDetails?.email ?: "---"
        binding.tvname.text = engineerName
        binding.tvEngineerContact.text = engineerPhone
        binding.tvEngineerEmail.text = engineerEmail
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(show: Boolean) {
        binding.progressBar.isVisible = show
        binding.groupContent.isVisible = !show

    }
}
