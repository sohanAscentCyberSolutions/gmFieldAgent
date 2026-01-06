import Status.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.md3.R
import com.example.md3.data.model.mrn.Item
import com.example.md3.data.model.mrn.SubmitMrnResponseItem
import com.example.md3.data.model.mrn.MrnItem
import com.example.md3.data.model.mrn.MrnSubmitResponse
import com.example.md3.databinding.FragmentSelectedMRNBinding
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.ViewUtils
import com.example.md3.view.breakdown.adapters.SelectedMRNAdapter
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class SelectedMRNFragment : Fragment() {
    // Binding object
    private var _binding: FragmentSelectedMRNBinding? = null
    private var selectedMRNAdapter = SelectedMRNAdapter()
    private lateinit var sharedViewModel: SharedViewModel
    private val args: SelectedMRNFragmentArgs by navArgs()
    private val commissioningViewModel: CommissioningViewModel by inject()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedMRNBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        binding.include.topAppBar.title = "Create MRN"

        binding.include.topAppBar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAddParts.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            val result = combineValues(selectedMRNAdapter.currentList,args.serviceRequestId)
            commissioningViewModel.submitMrn(args.serviceRequestId , result)
        }
        observe()
    }

    private fun observe() {
        sharedViewModel.mrnSelectedQnty.observe(viewLifecycleOwner) {
            selectedMRNAdapter.submitList(it)
        }
        binding.include.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }





        binding.recyclerView.adapter = selectedMRNAdapter


        commissioningViewModel.getCreateMrnResponseLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    SUCCESS -> {
                        if (args.isFromBreakdown) {
                            findNavController().popBackStack(R.id.viewOpenCasesBreakDownFragment, false)
                        } else {
                            findNavController().popBackStack(R.id.viewOpenCasesFragment, false)
                        }
                        ViewUtils.showSnackbar(binding.root ,"Item added successfully")
                        sharedViewModel.mrnSelectedQnty.postValue(mutableListOf())
                    }
                    ERROR -> {
                        ViewUtils.showSnackbar(binding.root ,it.message.toString())
                    }
                    LOADING -> {

                    }
                }
            }
        }
    }



    fun combineValues(items: List<MrnItem>, serviceRequestId: String): MrnSubmitResponse {
        val itemList = items.map { Item(it.bomItemDetails.id, it.quantity) }
        return MrnSubmitResponse(serviceRequestId, itemList)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding
        _binding = null
    }

}
