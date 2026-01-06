package com.example.md3.view.breakdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.data.model.mrn.BomResult
import com.example.md3.databinding.BomDetailBottomsheetBinding
import com.example.md3.databinding.BomListItemBinding
import com.example.md3.utils.GenericPagingAdapter
import com.example.md3.utils.KotlinFunctions.addItemDecoration
import com.example.md3.view.commissioning.CommissioningViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class BomDetailsBottomSheet(private val product: String) : BottomSheetDialogFragment(){

    private lateinit var bomAdapter: GenericPagingAdapter<BomResult>
    private var _binding: BomDetailBottomsheetBinding? = null
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val binding get() = _binding!!


    companion object {
        const val VISIT_ID = "visit_id"
        const val TAG = "YourBottomSheetFragment"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BomDetailBottomsheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }







    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSheetBehavior()
        initRecyclerview()
        initAdapters()
        apiCall()
        observe()

        binding.title.setOnClickListener {
            dismiss()
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun setSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

    }

    private fun initRecyclerview() {
        binding.recyclerViewLayout.swipRefreshLayout.isEnabled = false
        binding.recyclerViewLayout.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initAdapters() {
        bomAdapter = GenericPagingAdapter(
            R.layout.bom_list_item,
            { view, item, position ->
                val binding = BomListItemBinding.bind(view)
                binding.title.text = item.bomItemDetails.productType
                binding.value.text = item.bomItemDetails.name
                binding.tvQuantityHeading.text = "Qty : ${item.quantity}"

            },
            { item ->

            }
        )

        bomAdapter.addLoadStateListener { loadStates ->

            val refreshState = loadStates.source.refresh
            binding.recyclerViewLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading

            when (refreshState) {
                is LoadState.Loading -> {
                    binding.recyclerViewLayout.progressBar.isVisible = true
                    binding.recyclerViewLayout.recyclerView.isVisible = false
                    binding.recyclerViewLayout.tvEmptyTitle.isVisible = false
                }

                is LoadState.NotLoading -> {
                    if (bomAdapter.itemCount == 0) {
                        binding.recyclerViewLayout.progressBar.isVisible = false
                        binding.recyclerViewLayout.recyclerView.isVisible = false
                        binding.recyclerViewLayout.tvEmptyTitle.isVisible = true
                        binding.recyclerViewLayout.tvEmptyTitle.text = "No Item's found"
                    } else {
                        binding.recyclerViewLayout.progressBar.isVisible = false
                        binding.recyclerViewLayout.recyclerView.isVisible = true
                        binding.recyclerViewLayout.tvEmptyTitle.isVisible = false
                    }
                }

                is LoadState.Error -> {
                    Toast.makeText(requireContext(), "Item size Error", Toast.LENGTH_SHORT).show()
                    binding.recyclerViewLayout.progressBar.isVisible = false
                    binding.recyclerViewLayout.recyclerView.isVisible = false
                    binding.recyclerViewLayout.tvEmptyTitle.isVisible = true
                    binding.recyclerViewLayout.buttonRetry.isVisible = true
                    binding.recyclerViewLayout.tvEmptyTitle.text =
                        getString(R.string.something_went_wrong)
                }

            }
        }


        binding.recyclerViewLayout.recyclerView.adapter = bomAdapter
        binding.recyclerViewLayout.recyclerView.addItemDecoration(requireContext())



        binding.recyclerViewLayout.buttonRetry.setOnClickListener {
            bomAdapter.retry()
        }
    }





    private fun observe() {
        commissioningViewModel.getBOMlist(product).observe(viewLifecycleOwner) {
            bomAdapter.submitData(lifecycle,it)
        }
    }

    private fun apiCall() {
    }


    private fun setValues(data: GetVisitDetailsResponse) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
