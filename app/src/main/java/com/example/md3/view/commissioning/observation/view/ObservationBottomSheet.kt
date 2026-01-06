package com.example.md3.view.commissioning.observation.view


import Status
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.md3.databinding.ObservationBottomsheetBinding
import com.example.md3.utils.Progressive
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class ObservationBottomSheet(private val onResult: (Boolean) -> Unit) : BottomSheetDialogFragment(),
    Progressive {

    private val commissioningViewModel: CommissioningViewModel by inject()
    private var commissionVisitId: String? = null
    private var _binding: ObservationBottomsheetBinding? = null
    private var observationID: String? = null
    private val binding get() = _binding!!

    companion object {
        const val COMMISSIONING_VISIT_ID = "commissioning_visit_id"
        const val OBSERVATION_ID = "observation_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commissionVisitId = arguments?.getString(COMMISSIONING_VISIT_ID)
        observationID = arguments?.getString(OBSERVATION_ID)

        _binding = ObservationBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSheetBehavior()
        setdata()
        binding.btnStartWork.setOnClickListener {
            if (validateInput()) {
                submitData()
            }
        }

        observe()
    }

    private fun setdata() {
        observationID?.let {
            commissioningViewModel.getObservationsDetailsById(it)
        }
    }


    private fun setSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    private fun observe() {
        commissioningViewModel.observationLiveData.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        ViewUtils.showToast(requireContext(), "Observation recorded successfully")
                        onResult(true)
                        dismiss()
                    }

                    Status.ERROR -> {
                        ViewUtils.showToast(requireContext(), it.message)
                        show(false)
                    }

                    Status.LOADING -> {
                        show(false)
                    }
                }
            }
        }



        commissioningViewModel.getObservationDetailsById.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.etInput.setText(it.data?.observation)
                        binding.btnStartWork.setText("Edit Observation")
                    }

                    Status.ERROR -> {
                        ViewUtils.showToast(requireContext(), it.message)
                        Log.d("TAG", "observe: " + it.message)
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }



        commissioningViewModel.editObservationLiveData.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        ViewUtils.showToast(requireContext(), "Observation Edited successfully")
                        onResult(true)
                        dismiss()
                    }

                    Status.ERROR -> {
                        ViewUtils.showToast(requireContext(), it.message)
                        show(false)
                    }

                    Status.LOADING -> {
                        show(false)
                    }
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val observation = binding.tilInput.editText?.text.toString().trim()

        return if (observation.isEmpty()) {
            binding.tilInput.error = "Observation cannot be empty"
            false
        } else {
            binding.tilInput.error = null
            true
        }
    }

    private fun submitData() {
        commissionVisitId?.let {
            if (observationID != null) {
                commissioningViewModel.editObservation(
                    it,
                    binding.etInput.text.toString(),
                    observationID!!
                )
            } else {
                commissioningViewModel.createObservation(
                    it,
                    binding.etInput.text.toString()
                )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun show(show: Boolean) {
//        binding.btnLayout.apply {
//            if (show) {
//                btnLogin.isClickable = false
//                progressBar.isVisible = true
//                btnLogin.text = ""
//            } else {
//                btnLogin.isClickable = true
//                progressBar.isVisible = false
//                btnLogin.text = context?.getString(R.string.record_observation)
//            }
//        }
    }
}
