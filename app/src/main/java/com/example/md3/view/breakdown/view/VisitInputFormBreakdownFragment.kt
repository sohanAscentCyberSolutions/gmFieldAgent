package com.example.md3.view.breakdown.view

import Status
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.utils.GenericDropDownAdapter
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.data.model.mrn.MrnResult
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.AddedRequiredPartLayoutBinding
import com.example.md3.databinding.DropdownItemLayoutBinding
import com.example.md3.databinding.FailureLayoutItemBinding
import com.example.md3.databinding.FragmentVisitInputFormBreakdownBinding
import com.example.md3.utils.CommonAdapter
import com.example.md3.utils.DismissBottomSheet
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.Progressive
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.TimePickerUtils
import com.example.md3.utils.UploadImageAdapter
import com.example.md3.utils.UploadViewModel
import com.example.md3.utils.ViewKotlinUtils.toFormattedDate
import com.example.md3.utils.ViewKotlinUtils.toFormattedTime
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject
import java.util.Date


class VisitInputFormBreakdownFragment : Fragment(), Progressive {

    private val TAG = "VisitFormFragment"
    private lateinit var binding: FragmentVisitInputFormBreakdownBinding
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val sharedPrefs: SharedPrefs by inject()
    private val uploadViewModel: UploadViewModel by activityViewModels()
    private lateinit var sharedViewModel: SharedViewModel
    private var imageAdapter: UploadImageAdapter? = null
    lateinit var soultionAdapter: GenericDropDownAdapter<String>
    lateinit var failureCategeryAdapter: GenericDropDownAdapter<String>
    lateinit var faultAdapter: GenericDropDownAdapter<String>
    lateinit var addedPartsAdapter: CommonAdapter
    lateinit var addedFailureAdapter: CommonAdapter
    private var uploadImageList: MutableList<Uri> = mutableListOf()
    private var physicalCheckPerformed: Boolean = false
    private var endScheduledWorkDate: String = ""
    private var serviceRequiredCheckPerformed: Boolean = false
    private val args: VisitInputFormBreakdownFragmentArgs by navArgs()
    val dismissBottomsheet = DismissBottomSheet(false,true) {
        if (it) {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVisitInputFormBreakdownBinding.inflate(inflater, container, false)
        return binding.root
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        initRv()
        initAddedPartsRv()
        initAddedFailureRv()
        clickableViews()
        val layoutBinder: (view: View, item: String) -> Unit = { view, item ->
            val binding = DropdownItemLayoutBinding.bind(view)
            binding.text.text = item
        }

        endScheduledWorkDate = TimePickerUtils.formatDateFromString(
            Date().toFormattedDate(),
            TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
            TimePickerUtils.DATE_FORMAT_FOR_BACKEND
        )

        binding.scheduleDateInput.setText(endScheduledWorkDate)



        soultionAdapter = GenericDropDownAdapter(
            requireContext(),
            R.layout.dropdown_item_layout,
            listOf("Solution 1", "Solution 2"),
            layoutBinder
        )
        binding.solutionAppliedAc.setAdapter(soultionAdapter)



        failureCategeryAdapter = GenericDropDownAdapter(
            requireContext(),
            R.layout.dropdown_item_layout,
            listOf("Electric Related Issue", "Moisture Issue", "Voltage Issue", "Other"),
            layoutBinder
        )
        binding.failureLayoutAutoCompleteTextView.setAdapter(failureCategeryAdapter)


        faultAdapter = GenericDropDownAdapter(
            requireContext(),
            R.layout.dropdown_item_layout,
            listOf("Machine Related", "Non-Machine Related"),
            layoutBinder
        )
        binding.faultAutoCompleteTextView.setAdapter(faultAdapter)

        observe()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initAddedPartsRv() {
        addedPartsAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                AddedRequiredPartLayoutBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is AddedRequiredPartLayoutBinding && item is MrnResult) {
                    binding.tvProductSerial.text = item.productSerial
                    binding.tvProductName.text = item.partName


                    binding.remove.setOnClickListener {
                        sharedViewModel.removeSelectedPart(item)
                    }
                    binding.viewsupplierInfo.setOnClickListener {
                        if (binding.viewsupplierInfo.text == context?.getString(R.string.view_supplier_information)) {

                        } else {

                        }
                    }

                    binding.isSupplerVisitRequired.setOnClickListener {

                    }
                }
            }
        )

        binding.requiredPartRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addedPartsAdapter
        }
    }


    private fun initAddedFailureRv() {
        addedFailureAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                FailureLayoutItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is FailureLayoutItemBinding && item is String) {
                    binding?.tvWhy?.text = "Why"
                    binding?.tvWhyDescription?.text = item

                }
            }
        )

        binding.requiredFailureRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addedFailureAdapter
        }

    }

    private fun observe() {


        uploadViewModel.selectedImagesListOf.observe(viewLifecycleOwner){list ->
            imageAdapter?.submitList(list)
            imageAdapter?.notifyDataSetChanged()
        }


        sharedViewModel.getSelectedPartsLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.requiredPartRV.isVisible = true
                addedPartsAdapter.submitList(it as List<Any>?)
                addedPartsAdapter.notifyDataSetChanged()
            }
        }



        sharedViewModel.getSelectedFailureLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.requiredFailureRV.isVisible = true
                binding.editFailure.isVisible = true
                binding.btnFailureReason.isVisible = false
                addedFailureAdapter.submitList(it as List<Any>?)
                addedFailureAdapter.notifyDataSetChanged()
            }else{
                binding.requiredFailureRV.isVisible = false
                binding.editFailure.isVisible = false
                binding.btnFailureReason.isVisible = true
            }
        }






        commissioningViewModel.endWorkLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        (requireActivity() as MainActivity).stopStopwatchAndShowTime(
                            it.data?.timeElapsed?.hours ?: 0, it.data?.timeElapsed?.minutes ?: 0
                        )

                        commissioningViewModel.submitVisitInputForBreakdown(
                            args.caseId,
                            binding.isAnotherVisitRequired.isChecked,
                            endScheduledWorkDate,
                            Date().toFormattedTime(),
                            if (binding.scheduleInputEditText.text.toString().trim().isNotEmpty()) {
                                TimePickerUtils.formatDateFromString(
                                    binding.scheduleInputEditText.text.toString().trim(),
                                    TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
                                    TimePickerUtils.DATE_FORMAT_FOR_BACKEND
                                )
                            } else {
                                ""
                            },
                            TimePickerUtils.convertTo24HourFormat(
                                binding.timeInputEditText.text.toString().trim()
                            ),
                            binding.visitCheckBox.isChecked,
                            "Self visit",
                            physicalCheckPerformed,
                            serviceRequiredCheckPerformed,
                            mutableListOf(),
                            binding.problemDesInputEditText.text.toString().trim(),
                            binding.engineerReviewInputEditText.text.toString().trim(),
                            binding.failureLayoutAutoCompleteTextView.text.toString().trim(),
                            binding.faultAutoCompleteTextView.text.toString().trim(),
                            mutableListOf(),
                            binding.solutionAppliedAc.text.toString().trim(),
                            binding.solutionDesInputEditText.text.toString().trim(),
                            KotlinFunctions.createImagePartsFromURI(requireContext(), imageAdapter?.currentList)
                        )


                    }

                    Status.ERROR -> {
                        show(false)
                        ViewUtils.showSnackbar(binding.root, it.message)
                    }

                    Status.LOADING -> {
                        show(true)
                    }
                }
            }
        }


        commissioningViewModel.visitInputLiveData.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                when (it.status) {
                    Status.SUCCESS -> {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            "VISIT_INPUT_STATUS",
                            "success"
                        )
                        commissioningViewModel.refreshClosedCaseList()
                        findNavController().popBackStack()
                    }

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun clickableViews() {


        binding.editFailure.setOnClickListener {
            binding.btnFailureReason.performClick()
        }


        binding.btnFailureReason.setOnClickListener {
            findNavController().navigate(R.id.action_global_failureReasonFragment)
        }

        binding.radioGrpPhysicalCheck.setOnCheckedChangeListener { group, checkedId ->
            physicalCheckPerformed = if (checkedId == binding.yesradioPhysicalCheck.id) {
                true
            } else {
                false
            }
        }


        binding.radioGrpServiceCheck.setOnCheckedChangeListener { group, checkedId ->
            serviceRequiredCheckPerformed = if (checkedId == binding.yesradioServiceCheck.id) {
                true
            } else {
                false
            }
        }


        binding.btynAddPart.setOnClickListener {
            findNavController().navigate(R.id.action_visitInputFormBreakdownFragment_to_addSuppilerInfoFragment)
        }



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dismissBottomsheet.show(childFragmentManager, "")
                }
            })



        binding.btnAddPhotoForSoution.setOnClickListener {
            (requireActivity() as MainActivity).showUploadSheet()
        }

        binding.toolbarLayout.topAppBar.title = "Visit Input"
        binding.toolbarLayout.topAppBar.setNavigationOnClickListener {
            dismissBottomsheet.show(childFragmentManager, "")
        }


        binding.timeInputEditText.setOnClickListener {
            TimePickerUtils.showTimePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.timeInputLayout.editText?.setText(time)
                    }
                })
        }

        binding.timeInputLayout.setEndIconOnClickListener {
            TimePickerUtils.showTimePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.timeInputLayout.editText?.setText(time)
                    }
                })
        }



        binding.scheduleInputLayout.setEndIconOnClickListener {
            TimePickerUtils.showDatePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.scheduleInputEditText.setText(time)
                    }
                },
                true
            )
        }


        binding.scheduleInputEditText.setOnClickListener {
            TimePickerUtils.showDatePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.scheduleInputEditText.setText(time)
                    }
                },
                true
            )
        }


        binding.timeInputLayout.setEndIconOnClickListener {
            TimePickerUtils.showTimePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.timeInputEditText.setText(time)
                    }
                })
        }


        binding.timeInputEditText.setOnClickListener {
            TimePickerUtils.showTimePicker(
                requireContext(),
                object : TimePickerUtils.TimePickerListener {
                    override fun onSelected(time: String) {
                        binding.timeInputEditText.setText(time)
                    }
                })
        }







        binding.isAnotherVisitRequired.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.otherInfoGroup.isVisible = isChecked
        }


        binding.btnSubmitVisitInput.setOnClickListener {
            if (validate()) {
                showAssignmentDialogBox()
            }
        }

    }


    private fun performVisitInput() {
        commissioningViewModel.startAndEndWork(
            args.caseId,
            null,
            TimePickerUtils.getCurrentTime()
        )
        // Call visitInput function
//        commissioningViewModel.visitInput(
//            args.caseId,
//            binding.isAnotherVisitRequired.isChecked,
//            TimePickerUtils.formatDateFromString(
//                Date().toFormattedDate(),
//                TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
//                TimePickerUtils.DATE_FORMAT_FOR_BACKEND
//            ),
//            Date().toFormattedTime(),
//            binding.statusInputEditText.text.toString().trim(),
//            binding.visitCheckBox.isChecked,
//            if (binding.scheduleInputEditText.text.toString().trim()
//                    .isNotEmpty()
//            ) TimePickerUtils.formatDateFromString(
//                binding.scheduleInputEditText.text.toString().trim(),
//                TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
//                TimePickerUtils.DATE_FORMAT_FOR_BACKEND
//            ) else "",
//            binding.timeInputEditText.text.toString().trim()
//        )
    }


    private fun validate(): Boolean {
        val endVisitDate = binding.scheduleDateInput.text.toString().trim()
        val scheduleDate = binding.scheduleInputEditText.text.toString().trim()
        val scheduleTime = binding.timeInputEditText.text.toString().trim()
        val engineerReview = binding.engineerReviewInputEditText.text.toString().trim()
        val problemDescription = binding.problemDesInputEditText.text.toString().trim()
        val failureReason = binding.failureLayoutAutoCompleteTextView.text.toString().trim()
        val fault = binding.faultAutoCompleteTextView.text.toString().trim()
        val solutionApplied = binding.solutionAppliedAc.text.toString().trim()
        val solutionDescription = binding.solutionDesInputEditText.text.toString().trim()

        var isValid = true


        if (binding.isAnotherVisitRequired.isChecked) {
            if (scheduleDate.isEmpty()) {
                binding.scheduleInputLayout.error = "Scheduled Date is required"
                isValid = false
            } else {
                binding.scheduleInputLayout.error = null
            }

            if (scheduleTime.isEmpty()) {
                binding.timeInputLayout.error = "Scheduled Time is required"
                isValid = false
            } else {
                binding.timeInputLayout.error = null
            }
        }

        if (engineerReview.isEmpty()) {
            binding.engineerReviewInputLayout.error = "Engineer Review is required"
            isValid = false
        } else {
            binding.engineerReviewInputLayout.error = null
        }

        if (problemDescription.isEmpty()) {
            binding.problemDesInputLayout.error = "Problem Description is required"
            isValid = false
        } else {
            binding.problemDesInputLayout.error = null
        }

        if (failureReason.isEmpty()) {
            binding.failureLayout.error = "Failure Reason is required"
            isValid = false
        } else {
            binding.failureLayout.error = null
        }

        if (fault.isEmpty()) {
            binding.faultLayout.error = "Fault is required"
            isValid = false
        } else {
            binding.faultLayout.error = null
        }

        if (solutionApplied.isEmpty()) {
            binding.scheduleInputLayout.error = "Solution Applied is required"
            isValid = false
        } else {
            binding.scheduleInputLayout.error = null
        }

        if (solutionDescription.isEmpty()) {
            binding.solutionDesInputLayout.error = "Solution Description is required"
            isValid = false
        } else {
            binding.solutionDesInputLayout.error = null
        }

        return isValid
    }


    private fun showAssignmentDialogBox() {
        com.example.md3.utils.ViewKotlinUtils.submitAssignmentAlert(
            requireContext(),
            positiveButtonClickListener = {
                performVisitInput()
            },
            negativeButtonClickListener = {

            }
        )
    }


    override fun onDestroy() {
        uploadViewModel.removeImages()
        super.onDestroy()
    }


    override fun show(show: Boolean) {

    }
}