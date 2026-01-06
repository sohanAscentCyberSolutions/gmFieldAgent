package com.example.md3.view.auth.view

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.asap.codenicely.pdf.gstinvoicing.free.mobile.easy.gst.invoice.quick.quickinvoice.gstinvoicing.utils.GenericDropDownAdapter
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.DropdownItemLayoutBinding
import com.example.md3.databinding.FragmentVisitFormBinding
import com.example.md3.utils.DismissBottomSheet
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.Progressive
import com.example.md3.utils.TimePickerUtils
import com.example.md3.utils.TimePickerUtils.convertTo24HourFormat
import com.example.md3.utils.UploadImageAdapter
import com.example.md3.utils.UploadViewModel
import com.example.md3.utils.ViewKotlinUtils.toFormattedDate
import com.example.md3.utils.ViewKotlinUtils.toFormattedTime
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject
import java.util.Date

class VisitFormFragment : Fragment(), Progressive {

    private  val TAG = "VisitFormFragment"
    private lateinit var binding: FragmentVisitFormBinding
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val sharedPrefs: SharedPrefs by inject()
    private val uploadViewModel: UploadViewModel by activityViewModels()
    private var imageAdapter: UploadImageAdapter? = null
    lateinit var visitStatusAdapter: GenericDropDownAdapter<String>
    private var uploadImageList: MutableList<Uri> = mutableListOf()
    private val args: VisitFormFragmentArgs by navArgs()
    val dismissBottomsheet = DismissBottomSheet(false, true){
        if(it){
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVisitFormBinding.inflate(inflater, container, false)
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
        initRv()
        clickableViews()
        val layoutBinder: (view: View, item: String) -> Unit = { view, item ->
            val binding = DropdownItemLayoutBinding.bind(view)
            binding.text.text = item
        }

        ViewUtils.showKeyboard(requireContext())
        binding.summaryInputLayout.requestFocus()





        visitStatusAdapter = GenericDropDownAdapter(
            requireContext(),
            R.layout.dropdown_item_layout,
            listOf("Commissioning Complete", "To be Continued" , "Not Staretd"),
            layoutBinder
        )
        binding.visitStatusInput.setAdapter(visitStatusAdapter)

        observe()
        super.onViewCreated(view, savedInstanceState)
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

        commissioningViewModel.endWorkLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        show(false)
                        KotlinFunctions.removeAllWorkStatus(sharedPrefs)
                        (requireActivity() as MainActivity).stopStopwatchAndShowTime(
                            it.data?.timeElapsed?.hours ?: 0, it.data?.timeElapsed?.minutes ?: 0
                        )
                        commissioningViewModel.visitInput(
                            args.caseId,
                            binding.isAnotherVisitRequired.isChecked,
                            TimePickerUtils.formatDateFromString(
                                Date().toFormattedDate(),
                                TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
                                TimePickerUtils.DATE_FORMAT_FOR_BACKEND
                            ),
                            Date().toFormattedTime(),
                            "Self visit",
                            binding.visitStatusInput.text.toString().trim(),
                            binding.summaryInputEditText.text.toString().trim(),
                            binding.statusInputEditText.text.toString().trim(),
                            binding.visitCheckBox.isChecked,
                            if (binding.scheduleInputEditText.text.toString().trim()
                                    .isNotEmpty()
                            ) TimePickerUtils.formatDateFromString(
                                binding.scheduleInputEditText.text.toString().trim(),
                                TimePickerUtils.DATE_FORMAT_FOR_DISPLAY,
                                TimePickerUtils.DATE_FORMAT_FOR_BACKEND
                            ) else "",
                            convertTo24HourFormat(binding.timeInputEditText.text.toString().trim()),
                            if(imageAdapter?.currentList?.isEmpty() == true){
                                null
                            }else{
                                KotlinFunctions.createImagePartsFromURI(requireContext(), imageAdapter?.currentList)
                            }
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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dismissBottomsheet.show(childFragmentManager,"")
                }
            })



        binding.btnAddPhoto.setOnClickListener {
            (requireActivity() as MainActivity).showUploadSheet()
        }

        binding.toolbarLayout.topAppBar.title = "Visit Input"

        binding.toolbarLayout.topAppBar.setNavigationOnClickListener {
            dismissBottomsheet.show(childFragmentManager,"")
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
//                val visitConfirmationBottomSheet = VisitConfirmationBottomSheet {
//                }
//                visitConfirmationBottomSheet.show(
//                    childFragmentManager,
//                    VisitConfirmationBottomSheet.TAG
//                )

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
        val visitSummary = binding.summaryInputEditText.text.toString().trim()
        val explainStatus = binding.statusInputEditText.text.toString().trim()
        val scheduleDate = binding.scheduleInputEditText.text.toString().trim()
        val scheduleTime = binding.timeInputEditText.text.toString().trim()
        val visitStatus = binding.visitStatusInput.text.toString().trim()

        var isValid = true


        if (visitSummary.isEmpty()) {
            binding.summaryInputEditText.error = "Visit Summary is required"
            isValid = false
        } else {
            binding.summaryInputEditText.error = null
        }

        if (visitStatus.isEmpty()) {
            binding.visitStatusLayout.error = "Visit Summary is required"
            isValid = false
        } else {
            binding.visitStatusLayout.error = null
        }

        if (explainStatus.isEmpty()) {
            binding.statusInputEditText.error = "Explain Status is required"
            isValid = false
        } else {
            binding.statusInputEditText.error = null
        }

        if (binding.isAnotherVisitRequired.isChecked) {
            if (scheduleDate.isEmpty()) {
                binding.scheduleInputEditText.error = "Scheduled Date is required"
                isValid = false
            } else {
                binding.scheduleInputEditText.error = null
            }

            if (scheduleTime.isEmpty()) {
                binding.timeInputEditText.error = "Scheduled Time is required"
                isValid = false
            } else {
                binding.timeInputEditText.error = null
            }
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