package com.example.md3.view.commissioning.view

import Status
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.utils.GenericDropDownAdapter
import com.codenicely.bank.loan.instantloan.cashpo.utils.StringUtils
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.databinding.DropdownItemLayoutBinding
import com.example.md3.databinding.FragmentConclusionFormBinding
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.Progressive
import com.example.md3.utils.UploadImageAdapter
import com.example.md3.utils.UploadViewModel
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import org.koin.android.ext.android.inject


class ConclusionFormFragment : Fragment(R.layout.fragment_conclusion_form) , Progressive {
    private val commissioningViewModel: CommissioningViewModel by inject()
    private var _binding: FragmentConclusionFormBinding? = null
    private val binding get() = _binding!!
    lateinit var conclutionReasonAdepter: GenericDropDownAdapter<String>
    private var imageAdapter: UploadImageAdapter? = null
    private var uploadImageList: MutableList<Uri> = mutableListOf()
    private val uploadViewModel: UploadViewModel by activityViewModels()
    private val args: ConclusionFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConclusionFormBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        clickableViews()
        initViews()
        initRv()
        initAdepters()
        observe()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRv() {
        imageAdapter = UploadImageAdapter(requireContext()){
            uploadViewModel.removeSelectedImage(position = it)
        }



        binding.imageRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter =  imageAdapter
            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun observe() {
//        uploadViewModel.viewState.asLiveData().observe(viewLifecycleOwner) { list ->
//            imageAdapter?.submitList(list.imageBitmaps)
//            if (list.imageUri.isNotEmpty()) {
//                uploadImageList = list.imageUri as MutableList<Uri>
//            }
//            imageAdapter?.notifyDataSetChanged()
//        }


        uploadViewModel.selectedImagesListOf.observe(viewLifecycleOwner){list ->
            imageAdapter?.submitList(list)
            imageAdapter?.notifyDataSetChanged()
        }

        commissioningViewModel.concludeLiveData.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            "CONCLUSION_STATUS",
                            "success"
                        )
                        findNavController().popBackStack()
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

    private fun initAdepters(){
        val layoutBinder: (view: View, item: String) -> Unit = { view, item ->
            val binding = DropdownItemLayoutBinding.bind(view)
            binding.text.text = item
        }
        conclutionReasonAdepter = GenericDropDownAdapter(
            requireContext(),
            R.layout.dropdown_item_layout,
            listOf("Customer Unhappy", "Did Not Receive SMS" , "Customer not on site" , "Other"),
            layoutBinder
        )
        binding!!.setisficationLayout.codeNotProvidedReason.setAdapter(conclutionReasonAdepter)
    }

    private fun initViews(){
        binding.submitbtnLayout.btnLogin.text = getString(R.string.submit)
        binding.toolbarLayout.topAppBar.title = getString(R.string.title_conclution_form)
    }

    private fun clickableViews() {
        binding.apply {
            binding.toolbarLayout.topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.btnAddPhoto.setOnClickListener {
            (requireContext() as MainActivity).openGallery()
        }


        binding.signatureLayout.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                binding.signatureLayout.tvCustomerSignature.visibility = View.GONE
                binding.signatureLayout.clearSign.visibility = View.VISIBLE
            }

            override fun onSigned() {

            }

            override fun onClear() {
                binding.signatureLayout.tvCustomerSignature.visibility = View.VISIBLE
                binding.signatureLayout.clearSign.visibility = View.GONE
            }
        })

        binding.setisficationLayout.cbCodeNotProvided.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.setisficationLayout.reasonLayout.isVisible = isChecked
        }

        binding.submitbtnLayout.btnLogin.setOnClickListener {
            if (validate()) {
                submitDetails()
            }
        }
    }

   private fun submitDetails(){
        val signatureData = KotlinFunctions.BitmapToFileConverter(requireContext() , binding.signatureLayout.signaturePad.signatureBitmap)
       val signaturePart = KotlinFunctions.createPartFromFile("signature", signatureData)

        commissioningViewModel.concludeCase(
            args.caseId,
            binding.ratingBar.rating.toInt().toString(),
            binding.customerFormLayout.etCustomerName.text.toString().trim(),
            binding.customerFormLayout.etCustomerEmail.text.toString().trim(),
            binding.customerFormLayout.etCustomerPhone.text.toString().trim(),
            binding.customerFormLayout.etCustomerDesignation.text.toString().trim(),
            signaturePart,
            binding.customerFormLayout.etCustomerRemarks.text.toString().trim()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun validate(): Boolean {
        val engineersRemarks = binding.etEngRemarks.text.toString().trim()
        val customerName = binding.customerFormLayout.etCustomerName.text.toString().trim()
        val customerEmail = binding.customerFormLayout.etCustomerEmail.text.toString().trim()
        val customerPhone = binding.customerFormLayout.etCustomerPhone.text.toString().trim()
        val customerDesignation = binding.customerFormLayout.etCustomerDesignation.text.toString().trim()
        val customerRemarks = binding.customerFormLayout.etCustomerRemarks.text.toString().trim()

        val setisficationCode = binding.setisficationLayout.etSetisficationCode.text.toString().trim()
        val codeNotProvidedReason = binding.setisficationLayout.codeNotProvidedReason.text.toString().trim()

        if (customerName.isEmpty()) {
           binding.customerFormLayout.etCustomerName.error = "Name required"
            binding.customerFormLayout.etCustomerName.requestFocus()
            return false
        } else {
            binding.customerFormLayout.etCustomerName.error = null
        }

        if (!StringUtils.EMAIL_ADDRESS_PATTERN.matcher(customerEmail).matches()) {
            binding.customerFormLayout.etCustomerEmail.error = "E-Mail required"
            binding.customerFormLayout.etCustomerEmail.requestFocus()
            return false
        } else {
            binding.customerFormLayout.etCustomerEmail.error = null
        }

        if (customerPhone.length < 10 ) {
            binding.customerFormLayout.etCustomerPhone.error = "Valid Mobile No required"
            binding.customerFormLayout.etCustomerPhone.requestFocus()
            return false
        } else {
            binding.customerFormLayout.etCustomerPhone.error = null
        }

        if (customerDesignation.isEmpty() ) {
            binding.customerFormLayout.etCustomerDesignation.error = "Designation required"
            binding.customerFormLayout.etCustomerDesignation.requestFocus()
            return false
        } else {
            binding.customerFormLayout.etCustomerDesignation.error = null
        }

        if (customerRemarks.isEmpty() ) {
            binding.customerFormLayout.etCustomerRemarks.error = "Remarks required"
            binding.customerFormLayout.etCustomerRemarks.requestFocus()
            return false
        } else {
            binding.customerFormLayout.etCustomerRemarks.error = null
        }

       /* if (setisficationCode.isEmpty() && !binding.setisficationLayout.cbCodeNotProvided.isChecked) {
            binding.setisficationLayout.etSetisficationCode.error = "Satisfaction Code required"
            isValid = false
        } else {
            binding.setisficationLayout.etSetisficationCode.error = null
        }

        if (codeNotProvidedReason.isEmpty() && binding.setisficationLayout.cbCodeNotProvided.isChecked) {
            binding.setisficationLayout.codeNotProvidedReason.error = "Reason required"
            isValid = false
        } else {
            binding.setisficationLayout.codeNotProvidedReason.error = null
        }*/

        if (binding.signatureLayout.signaturePad.isEmpty){
            return false
            ViewUtils.showSnackbar(binding.root ,"Customer Signature Required")
        }

        return true
    }

    override fun show(show: Boolean) {
        binding.submitbtnLayout.apply {
            if (show) {
                btnLogin.isClickable = false
                progressBar.isVisible = true
                btnLogin.text = ""
            } else {
                btnLogin.isClickable = true
                progressBar.isVisible = false
                btnLogin.text = getString(R.string.login)
            }
        }
    }

}