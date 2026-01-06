package com.example.md3.view.breakdown.view

import Status
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.md3.data.model.commissioning.conclude.ConcludeResponse
import com.example.md3.databinding.ConclusionDetailsBottomSheetBinding
import com.example.md3.utils.DisplayImageAdapter
import com.example.md3.utils.Progressive
import com.example.md3.utils.glide.GlideImageLoader
import com.example.md3.view.commissioning.CommissioningViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject


class ConclusionDetailsBottomSheet : BottomSheetDialogFragment(), Progressive {

    private var _binding: ConclusionDetailsBottomSheetBinding? = null
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val binding get() = _binding!!
    private var orgCommID: String? = null

    private val imageAdapter: DisplayImageAdapter by lazy { DisplayImageAdapter(requireContext()) }

    companion object {
        const val ORG_COMMISSIONING_ID = "org_commissioning_id"
        const val TAG = "YourBottomSheetFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ConclusionDetailsBottomSheetBinding.inflate(inflater, container, false)
        orgCommID = arguments?.getString(ORG_COMMISSIONING_ID)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRv()
        setSheetBehavior()
        apiCall()
        observe()
        clickableViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun clickableViews() {
        binding.apply {
            btnBack.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun observe() {
        commissioningViewModel.getConclusionDetailsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                        it.data?.let { it1 ->
                            setValues(it1)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        show(false)
                    }

                    Status.LOADING -> {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()

                        show(true)
                    }
                }
            }
        }
    }

    private fun apiCall() {
        orgCommID?.let {
            commissioningViewModel.getConclusionDetails(it) }
    }

    private fun initRv() {
        binding.contentViewEngiInput.imageRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter =  imageAdapter
            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun setSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    private fun setValues(data: ConcludeResponse) {
        //engineer's remark need to add once it added in the api
        // binding.contentViewEngiInput.tvengineerRemark.text = "${data.customer_remark}"
        binding.contentviewCustInfo.tvCustomerName.text = "${data.name}"
        binding.contentviewCustInfo.tvCustomerDesignation.text = "${data.designation}"
        binding.contentviewCustInfo.tvCustomerEmail.text = "${data.email}"
        binding.contentviewCustInfo.tvCustomerPhone.text = "${data.phone}"
        binding.contentviewCustInfo.tvCustomerRemark.text = "${data.customer_remark}"
        binding.contentviewCustInfo.tvCustomerFeedback.text = "${data.customer_feedback}"
        GlideImageLoader(context).loadImage(data.customer_signature , binding.contentviewCustInfo.ivCustomerSign , ProgressBar(requireContext()))
//        imageAdapter.submitList(data.images)
//        imageAdapter.notifyDataSetChanged()
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
