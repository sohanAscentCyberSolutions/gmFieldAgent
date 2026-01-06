import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.checksheet.Field
import com.example.md3.data.model.checksheet.Section
import com.example.md3.data.model.checksheet.SubmitCheekSheet
import com.example.md3.databinding.FragmentViewCheckSheetBinding
import com.example.md3.utils.DismissBottomSheet
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.KotlinFunctions.addItemDecoration
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.ViewUtils
import com.example.md3.view.checksheet.adapter.SubmitCheckSheetAdapter
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject


class ViewCheckSheetFragment : Fragment() {

    private var _binding: FragmentViewCheckSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private  val TAG = "ViewCheckSheetFragment"
    private val args: ViewCheckSheetFragmentArgs by navArgs()
    private var submitCheckSheetAdapter: SubmitCheckSheetAdapter? = null
    private val commissioningViewModel: CommissioningViewModel by inject()
    private var getCheckSheetCallExecuted = false
    private var isCheckSheetEdited : Boolean = false
    val dismissBottomsheet = DismissBottomSheet(true){
        if(it){
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewCheckSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            getCheckSheetCallExecuted = it.getBoolean("GETCHECKSHEETCALLEXECUTED", false)
        }
        initViewModels()
        initToolbar()
        initRecyclerView()
        observeViewModels()
        handleViewsClick()
        performAPICall()
    }

    private fun initViewModels() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        submitCheckSheetAdapter = SubmitCheckSheetAdapter { section ->
            isCheckSheetEdited = true
            sharedViewModel.setSectionResponse(section)
            val action = ViewCheckSheetFragmentDirections.actionSubmitCheckSheetFragmentToCheckSheetChipDetailsFragment(
                context?.getString(R.string.view_action_section) ?: "",
                section.section
            )
            findNavController().navigate(action)
        }
    }

    private fun initToolbar() {
        binding.toolbarLayout.topAppBar.title = "Submit CheckSheet"
        binding.toolbarLayout.topAppBar.setNavigationOnClickListener {
            if(isCheckSheetEdited){
                dismissBottomsheet.show(childFragmentManager,"")
            }else{
                findNavController().popBackStack()
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = submitCheckSheetAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(requireContext())
    }

    private fun handleViewsClick() {




        binding.btnViewOverall.setOnClickListener {
            isCheckSheetEdited = true
            val action = ViewCheckSheetFragmentDirections.actionSubmitCheckSheetFragmentToCheckSheetDetailsFragment(
                context?.getString(R.string.view_action_overall) ?: "", "Overall"
            )
            findNavController().navigate(action)
        }

        binding.btnSubmit.setOnClickListener{
            val allFields = submitCheckSheetAdapter?.currentList?.let { it1 -> getAllFields(it1) }
            allFields?.let {
                val submitCheekSheet = SubmitCheekSheet(it)
                commissioningViewModel.submitCheckSheet(orgCommissioningID = args.visitId , submitCheekSheet)
            }
        }
    }

    private fun performAPICall() {
        if (!getCheckSheetCallExecuted) {
            commissioningViewModel.getAllCheckSheet(args.visitId)
        }
    }




    fun getAllFields(sections: List<Section>): List<Field> {
        val allFields = mutableListOf<Field>()
        sections.forEach { section ->
            section.subSections.forEach { subSection ->
                allFields.addAll(subSection.fields)
            }
        }
        return allFields
    }




    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("GETCHECKSHEETCALLEXECUTED", getCheckSheetCallExecuted)
        super.onSaveInstanceState(outState)
    }

    private fun observeViewModels() {

        commissioningViewModel.getSubmitCheckSheetLiveData.observe(viewLifecycleOwner){ response ->
            if (!response.isResponseHandled()) {
                when (response.status) {
                    Status.SUCCESS -> {
                       ViewUtils.showSnackbar(binding.btnSubmit , "CheckSheet Submitted Successfully")
                        findNavController().popBackStack()
                    }

                    Status.ERROR -> {
                        ViewUtils.showSnackbar(binding.btnSubmit , "${response.message}")
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }




        commissioningViewModel.getAllCheckSheetLiveData.observe(viewLifecycleOwner) { response ->
            if (!response.isResponseHandled()) {
                when (response.status) {
                    Status.SUCCESS -> {
                        binding.tvEmptyTitle.isVisible = false
                        binding.loadingProgress.isVisible = false
                        binding.recyclerView.isVisible = true
                        binding.btnViewOverall.isVisible = true
                        binding.materialTextView.isVisible = true
                        binding.progressBar.isVisible = true

                        response.data?.let { result ->
                            getCheckSheetCallExecuted = true
                            sharedViewModel.setCheckSheetResponse(result)
                        }
                    }

                    Status.ERROR -> {
                        binding.btnSubmit.isVisible = false
                        binding.tvEmptyTitle.isVisible = true
                        binding.textViewProgress.isVisible = false
                        binding.tvEmptyTitle.text = response.message
                        binding.loadingProgress.isVisible = false
                        binding.recyclerView.isVisible = false
                        binding.btnViewOverall.isVisible = false
                        binding.materialTextView.isVisible = false
                        binding.progressBar.isVisible = false
                        getCheckSheetCallExecuted = false
                    }

                    Status.LOADING -> {
                        binding.textViewProgress.isVisible = false
                        binding.tvEmptyTitle.isVisible = false
                        binding.loadingProgress.isVisible = true
                        binding.recyclerView.isVisible = false
                        binding.btnViewOverall.isVisible = false
                        binding.materialTextView.isVisible = false
                        binding.progressBar.isVisible = false
                    }
                }
            }
        }

        sharedViewModel.setCheckSetMutableLiveData.observe(viewLifecycleOwner) { result ->
            submitCheckSheetAdapter?.submitList(result?.sections)
            val total = result?.countNullFields()?.toDouble()?.times(100)
            val nonnull = result?.countNonNullFields()?.toDouble()?.times(100)
            val doneProgress = result?.calculatePercentage(nonnull!!, total!!)?.toInt()
            binding.materialTextView.text = "${result?.countNonNullFields()} / ${result?.countNullFields()}  Completed"
            doneProgress?.let {
                val progressColor = KotlinFunctions.setProgressAndColor(doneProgress)
                binding.progressBar.progress = it
                binding.textViewProgress.text = "$it%"
                binding.progressBar.setIndicatorColor(ContextCompat.getColor(binding.root.context, progressColor))
                binding.textViewProgress.setTextColor(ContextCompat.getColor(requireContext(), progressColor))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

