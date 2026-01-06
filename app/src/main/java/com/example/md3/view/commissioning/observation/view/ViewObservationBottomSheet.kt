package com.example.md3.view.commissioning.observation.view


import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.observation.ObservationResult
import com.example.md3.databinding.ObservationItemLayoutBinding
import com.example.md3.databinding.ViewObservationsBottomsheetBinding
import com.example.md3.utils.GenericPagingAdapter
import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.visit.VisitInputDetailsBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class ViewObservationBottomSheet : BottomSheetDialogFragment() {

    private var _binding: ViewObservationsBottomsheetBinding? = null
    lateinit var observationPagingAdapter: GenericPagingAdapter<ObservationResult>
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val binding get() = _binding!!
    private var visitID: String? = null


    companion object {
        const val VISIT_ID = "visit_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        visitID = arguments?.getString(VisitInputDetailsBottomSheet.VISIT_ID)
        _binding = ViewObservationsBottomsheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSheetBehavior()
        initRecyclerview()
        initAdapter()
        clickableViews()
        observe()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun clickableViews() {
        binding.tvHeading.setOnClickListener {
            dismiss()
        }

        binding.addbtn.setOnClickListener {
            showObservationBottomSheet()
        }


        binding.recyclerViewLayout.swipRefreshLayout.setOnRefreshListener {
            observationPagingAdapter.refresh()
            binding.recyclerViewLayout.swipRefreshLayout.isRefreshing = false
        }


    }

    private fun observe() {
        visitID?.let {
            commissioningViewModel.getObservationByVisitID(it).observe(viewLifecycleOwner) { data ->
                observationPagingAdapter.submitData(lifecycle, data)
            }
        }
    }

    private fun initAdapter() {
        observationPagingAdapter = GenericPagingAdapter(
            R.layout.observation_item_layout,
            { view, item, position ->
                val binding = ObservationItemLayoutBinding.bind(view)
                val count = position?.plus(1)
                binding.tvObservationHeading.text = "Observation ${count.toString()}"
                binding.tvObservationdes.text = item.observation


                binding.editObservation.setOnClickListener {
                    val observationBottomSheet = ObservationBottomSheet { isSuccess ->
                        if (isSuccess) {
                            observe()
                        }
                    }
                    val bundle = Bundle()
                    bundle.putString(ObservationBottomSheet.COMMISSIONING_VISIT_ID, VISIT_ID)
                    bundle.putString(ObservationBottomSheet.OBSERVATION_ID, item.id)
                    observationBottomSheet.arguments = bundle
                    observationBottomSheet.show(childFragmentManager, "")

                }
            },
            { item ->

            }
        )
        binding.recyclerViewLayout.recyclerView.adapter = observationPagingAdapter
    }


    private fun initRecyclerview() {
        binding.recyclerViewLayout.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun setSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

    }


    private fun showObservationBottomSheet() {
        val observationBottomsheet = ObservationBottomSheet { isSuccess ->
            if (isSuccess) {
                initAdapter()
                observe()
            }
        }
        val bundle = Bundle()
        bundle.putString(
            ObservationBottomSheet.COMMISSIONING_VISIT_ID,
            visitID
        )
        observationBottomsheet.arguments = bundle
        observationBottomsheet.show(childFragmentManager, "Sheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
