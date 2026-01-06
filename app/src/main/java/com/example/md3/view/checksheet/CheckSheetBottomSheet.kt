package com.example.md3.view.checksheet

import Status
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.databinding.ChecksheetBottomsheetBinding
import com.example.md3.utils.Progressive
import com.example.md3.utils.ViewUtils
import com.example.md3.view.checksheet.adapter.SectionAdapter
import com.example.md3.view.commissioning.CommissioningViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject


class CheckSheetBottomSheet : BottomSheetDialogFragment(), Progressive {

    private val commissioningViewModel: CommissioningViewModel by inject()
    private var commissionVisitId: String? = null
    private var _binding: ChecksheetBottomsheetBinding? = null
    var checkSheetParentAdapter = SectionAdapter(false)
    private val binding get() = _binding!!

    companion object {
        const val COMMISSIONING_VISIT_ID = "commissioning_visit_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commissionVisitId = arguments?.getString(COMMISSIONING_VISIT_ID)
        _binding = ChecksheetBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        apiCall()
        setSheetBehavior()
        clickableViews()
        observe()
    }

    private fun clickableViews() {
        binding.topAppBar.title = "View CheckSheet"
        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun apiCall() {
        commissionVisitId?.let { commissioningViewModel.getAllCheckSheet(it) }
    }

    private fun initRv() {
        binding.apply {
            parentRv.layoutManager = LinearLayoutManager(requireContext())
            parentRv.adapter = checkSheetParentAdapter
        }
    }


    private fun setSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    private fun observe() {
        commissioningViewModel.getAllCheckSheetLiveData.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
//                        show(false)
                        checkSheetParentAdapter.submitList(it.data?.sections)
                    }

                    Status.ERROR -> {
//                        show(false)
                        ViewUtils.showSnackbar(binding.parentRv.rootView,it.message)
                    }

                    Status.LOADING -> {
//                        show(true)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun show(show: Boolean) {
        binding.parentRv.isVisible = !show
        binding.progressBar.isVisible = show
    }
}
