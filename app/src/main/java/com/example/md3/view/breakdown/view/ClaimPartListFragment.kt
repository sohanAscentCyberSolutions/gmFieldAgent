package com.example.md3.view.breakdown.view

import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.mrn.MrnDetailsItem
import com.example.md3.data.model.mrn.PartResultItem
import com.example.md3.databinding.FragmentCreateMRNBinding
import com.example.md3.databinding.MrnListItemLayoutBinding
import com.example.md3.utils.CommonAdapter
import com.example.md3.utils.KotlinFunctions.addItemDecoration
import com.example.md3.utils.SharedViewModel
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class ClaimPartListFragment(private val serviceRequestId : String , private val isFromBreakdown : Boolean) : DialogFragment() {
    private lateinit var mrnCommonAdapter: CommonAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private val commissioningViewModel: CommissioningViewModel by inject()
    private var _binding: FragmentCreateMRNBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme)
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
                    commissioningViewModel.getAllPartList(
                        serviceRequestId,
                        it
                    )
                }
                return true
            }
        })


    }

    private fun observe() {

        commissioningViewModel.getAllPartList(serviceRequestId, "")


        commissioningViewModel.getPartsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            mrnCommonAdapter.submitList(it)
                        }
                    }

                    Status.ERROR -> {
                        mrnCommonAdapter.submitList(emptyList())
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun initToolbar() {
        binding.toolbarlayout.apply {
            binding.topAppBar.setNavigationOnClickListener {
//                findNavController().popBackStack()
                dismiss()
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
                if (binding is MrnListItemLayoutBinding && item is PartResultItem) {

                    binding.tvProductSerial.text = item.bomItemDetails.name
                    binding.tvProductName.isVisible = false


                    if (item.bomItemDetails.warranty != 0) {
                        binding.chipInWarranty.isVisible = true
                        binding.chipOutWarranty.isVisible = false
                    } else {
                        binding.chipInWarranty.isVisible = false
                        binding.chipOutWarranty.isVisible = true
                    }

                    binding.addbtn.setOnClickListener {
                        val mrnDetailsItem = MrnDetailsItem(
                            productDetails = item.bomItemDetails.toProductDetails(),
                            quantity = item.quantity,
                        )

                        sharedViewModel.mrnDetailsItemSelected.postValue(mrnDetailsItem)
                        dismiss()
//                        val action = ClaimPartListFragmentDirections.actionGlobalClaimPartFragment(
//                            serviceRequestId = args.serviceRequestId,
//                            isFromBreakdown = args.isFromBreakdown
//                        )
//                        findNavController().navigate(action)
                    }
                }
            }
        )


        binding.recyclerView.adapter = mrnCommonAdapter
        binding.recyclerView.addItemDecoration(requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}