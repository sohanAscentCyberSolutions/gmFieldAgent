package com.example.md3.view.breakdown.view


import Status
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.data.model.mrn.MrnDetailsItem
import com.example.md3.databinding.MrnAlreadyAddedpartsItemBinding
import com.example.md3.databinding.ViewMrnPartsBottomsheetBinding
import com.example.md3.utils.CommonAdapter
import com.example.md3.view.commissioning.CommissioningViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class ShowMRNPartsBottomSheet(private val serviceReqId : String) : BottomSheetDialogFragment() {

    private var _binding: ViewMrnPartsBottomsheetBinding? = null
    private val commissioningViewModel : CommissioningViewModel by inject()
    private lateinit var mrnPartsListAdapter : CommonAdapter
    private var mrnPartsList: List<MrnDetailsItem> = emptyList()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewMrnPartsBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSheetBehavior()
        apiCall()
        initRecyclerView()
        initAdapter()
        observe()
        clickableViews()
    }

    private fun clickableViews() {
        binding.imageView6.setOnClickListener {
            dismiss()
        }
    }

    private fun observe() {

        commissioningViewModel.mrnDetailsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            mrnPartsListAdapter.submitList(it)
                        }
                    }

                    Status.ERROR -> {
                        mrnPartsListAdapter.submitList(emptyList())
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun apiCall() {
        commissioningViewModel.getAllMrnDetails(serviceReqId, "")
    }

    private fun initAdapter() {

        mrnPartsListAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                MrnAlreadyAddedpartsItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is MrnAlreadyAddedpartsItemBinding && item is MrnDetailsItem) {
                    binding.tvProductSerial.text = item.productDetails.modelNumber
                    binding.tvProductName.text = item.productDetails.name
                    binding.tvQuantity.text = item.quantity.toString()
                    binding.tvProductName.isVisible = item.productDetails.modelNumber.isNotBlank()
                }
            }
        )



        mrnPartsListAdapter.submitList(mrnPartsList)
        binding.recyclerViewLayout.recyclerView.adapter = mrnPartsListAdapter
    }

    private fun initRecyclerView() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
