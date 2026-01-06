package com.example.md3.view.breakdown.view

import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.data.model.mrn.MrnItem
import com.example.md3.databinding.FragmentCreateMRNBinding
import com.example.md3.databinding.MrnListItemLayoutBinding
import com.example.md3.utils.CommonAdapter
import com.example.md3.utils.SharedViewModel
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateMRNFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val args: CreateMRNFragmentArgs by navArgs()
    private lateinit var mrnCommonAdapter: CommonAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private val commissioningViewModel: CommissioningViewModel by inject()
    private var _binding: FragmentCreateMRNBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateMRNBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        initToolbar()
        initRecyclerview()
        initAdapters()
        observe()



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    commissioningViewModel.getAllMrnProductList(
                        args.serviceRequestId,
                        it
                    )
                }
                return true
            }
        })


    }

    private fun observe() {

        commissioningViewModel.getAllMrnProductList(args.serviceRequestId,"")


        commissioningViewModel.mrnMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            binding.recyclerView.isVisible = true
                            binding.tvEmptyTitle.isVisible = false
                            binding.progressBar.isVisible = false
                            if(it.isEmpty()){
                                binding.tvEmptyTitle.isVisible = true
                                binding.tvEmptyTitle.text = "No Mrn Available"
                            }else{
                                binding.tvEmptyTitle.isVisible = false
                                mrnCommonAdapter.submitList(it)
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.recyclerView.isVisible = false
                        binding.tvEmptyTitle.isVisible = true
                        binding.progressBar.isVisible = false
                        binding.tvEmptyTitle.text = it.message
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        mrnCommonAdapter.submitList(emptyList())
                    }
                    Status.LOADING -> {
                        binding.recyclerView.isVisible = false
                        binding.tvEmptyTitle.isVisible = false
                        binding.progressBar.isVisible = true

                    }
                }
            }
        }
    }

    private fun initToolbar() {
        binding.toolbarlayout.apply {
            binding.topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun initRecyclerview() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun initAdapters() {

        mrnCommonAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                MrnListItemLayoutBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is MrnListItemLayoutBinding && item is MrnItem) {

                    if(item.bomItemDetails.partNumber.isNullOrBlank()){
                        binding.tvProductSerial.isVisible = false
                    }else{
                        binding.tvProductSerial.text = item.bomItemDetails.partNumber.toString()
                    }

                    binding.tvProductName.text = item.bomItemDetails.name
                    if (item.warrantyDetails.status != "Out of Warranty" || item.warrantyDetails.coverageMonths != 0) {
                        binding.chipInWarranty.isVisible = true
                        binding.addbtn.isEnabled = true
                        binding.chipOutWarranty.isVisible = false
                    } else {
                        binding.addbtn.isEnabled = false
                        binding.addbtn.isClickable = true
                        binding.chipInWarranty.isVisible = false
                        binding.chipOutWarranty.isVisible = true
                    }

                    binding.addbtn.setOnClickListener {
                        item.userEnteredQnty  = 1
                        sharedViewModel.addItemToList(item)
                        val action = CreateMRNFragmentDirections.actionCreateMRNFragmentToSelectedMRNFragment(
                                isFromBreakdown = args.isFromBreakdown,
                                serviceRequestId = args.serviceRequestId
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        )


        binding.recyclerView.adapter = mrnCommonAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.mrnSelectedQnty.postValue(mutableListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateMRNFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
