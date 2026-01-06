package com.example.md3.view.breakdown.view

import Status
import android.app.Dialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.underline
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.data.model.product.ProductDetailTitleAndValue
import com.example.md3.data.model.product.ProductDetails
import com.example.md3.databinding.ProductDetailsBottomsheetBinding
import com.example.md3.databinding.ProductListItemBinding
import com.example.md3.utils.CommonAdapter
import com.example.md3.utils.KotlinFunctions.addItemDecoration
import com.example.md3.utils.Progressive
import com.example.md3.view.commissioning.CommissioningViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class ProductDetailsBottomSheet(private val product: String) : BottomSheetDialogFragment(),
    Progressive {

    private lateinit var mrnCommonAdapter: CommonAdapter
    private var _binding: ProductDetailsBottomsheetBinding? = null
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val binding get() = _binding!!
    private var visitID: String? = null


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
        visitID = arguments?.getString(VISIT_ID)
        _binding = ProductDetailsBottomsheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isDraggable = false
        this.isCancelable = true
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        view.layoutParams.height = getScreenHeight().times(0.9).roundToInt()
        apiCall()
        observe()
        clickableViews()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun clickableViews() {
        binding.apply {
            btnDone.setOnClickListener {
                dismiss()
            }
        }
    }


    private fun initAdapters(it1: ProductDetails) {

        mrnCommonAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                ProductListItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is ProductListItemBinding && item is ProductDetailTitleAndValue) {
                    binding.title.text = item.title
                    binding.value.text = item.value

                    if(item.title == "Bill Of Material"){
                        binding.value.text = SpannableStringBuilder()
                            .color(getColor(binding.root.context, R.color.md_theme_secondary)) {
                                bold { underline { append(item.value) } }
                            }
                    }


                    binding.root.setOnClickListener {
                        if(item.title == "Bill Of Material"){
                            val bomDetailsBottomSheet = BomDetailsBottomSheet(product)
                            bomDetailsBottomSheet.show(childFragmentManager,"")
                        }
                    }

                }



            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = mrnCommonAdapter
        binding.recyclerView.addItemDecoration(requireContext())
        mrnCommonAdapter.submitList(it1.toListOfTitleAndValue())



    }


    private fun observe() {
        commissioningViewModel.getProductDetailsLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        it.data?.let { it1 ->
                            initAdapters(it1)
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
        commissioningViewModel.getProductDetails(product)
    }


    private fun setSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.root.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

    }

    private fun setValues(data: GetVisitDetailsResponse) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(show: Boolean) {
        binding.progressBar.isVisible = show
    }
}
