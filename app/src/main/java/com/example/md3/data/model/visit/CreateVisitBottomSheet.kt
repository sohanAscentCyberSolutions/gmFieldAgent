package com.example.md3.data.model.visit


import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.CreateVisitBottomsheetBinding
import com.example.md3.utils.TimePickerUtils
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject


class CreateVisitBottomSheet(
    private val isForEdit : Boolean = false,
    private val closedCasesResult: ServiceRequestDetails,
    private val onClick: () -> Unit
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: CreateVisitBottomsheetBinding
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val sharedPrefs: SharedPrefs by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateVisitBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        commissioningViewModel.getCreateVisitLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            dismiss()
                            onClick()
                        }
                    }

                    Status.ERROR -> {
                        ViewUtils.showSnackbar(binding.btnStartWork, it.message)
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }


        binding.scheduleTimeInput.setOnClickListener {
            TimePickerUtils.showTimePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.scheduleTimeInput.setText(time)
                    }
                })
        }


        binding.scheduleTimeLayout.setEndIconOnClickListener {
            TimePickerUtils.showTimePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.scheduleTimeInput.setText(time)
                    }
                })
        }


        binding.scheduleDateLayout.setEndIconOnClickListener {
            TimePickerUtils.showDatePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.scheduleDateInput.setText(time)
                    }
                },
                true
            )
        }


        binding.scheduleDateInput.setOnClickListener {

            TimePickerUtils.showDatePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.scheduleDateInput.setText(time)
                    }
                },
                true
            )
        }




        binding.btnStartWork.setOnClickListener {
            if (validateInputs()) {
                val input1 = binding.scheduleDateInput.text.toString()
                val input2 = binding.scheduleTimeInput.text.toString()
                closedCasesResult.commissioningTypeDetails?.id
                closedCasesResult.customerDetails.name
                val data =
                    CreateVisitSubmitResponse(
                        assignedEngineer = sharedPrefs.organisationUserId,
                        serviceRequest = closedCasesResult.id,
                        type = "",
                        remark = "Self Assigned",
                        scheduledDate = TimePickerUtils.formatDateFromString(
                            input1,
                            TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
                            TimePickerUtils.DATE_FORMAT_FOR_BACKEND
                        ),
                        scheduledTime = TimePickerUtils.convertTo24HourFormat(
                            input2
                        ),
                        visitAddress = closedCasesResult.customerAddress.toVisitAddress()
                    )

                commissioningViewModel.createVisit(data)

            }
        }
    }

    private fun validateInputs(): Boolean {
        val input1 = binding.scheduleDateInput.text.toString()
        val input2 = binding.scheduleTimeInput.text.toString()

        if (input1.isEmpty()) {
            binding.scheduleDateLayout.error = "Fields can't be empty"
            return false
        }

        if (input2.isEmpty()) {
            binding.scheduleTimeLayout.error = "Fields can't be empty"
            return false
        }
        return true
    }
}

