package com.example.md3.view.breakdown.view

import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.md3.data.model.amc.ContractDetails
import com.example.md3.data.model.warranty.WarrantyInfo
import com.example.md3.databinding.WarrantyDetailsFragmentBinding
import com.example.md3.utils.CommonAdapter
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class WarrantyAndOtherDetailsFragment : Fragment() {
    private lateinit var mrnCommonAdapter: CommonAdapter
    private var _binding: WarrantyDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val args : WarrantyAndOtherDetailsFragmentArgs by navArgs()
    private val commissioningViewModel: CommissioningViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WarrantyDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarlayout.topAppBar.title  = "Warranty & Other Details"
        api()
        observe()
        setupClickListeners()

    }

    private fun observe() {
        commissioningViewModel.getAmcDetailsLiveData.observe(viewLifecycleOwner){
            if(!it.isResponseHandled()){
                when(it.status){
                    Status.SUCCESS ->{
                        binding.progressBar.isVisible = false
                        binding.amcLayout.root.isVisible = true
                        updateAMCUi(it.data)
                    }
                    Status.ERROR -> {
                        binding.amcLayout.root.isVisible = false
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                        binding.progressBar.isVisible = false
                    }
                    Status.LOADING -> {
                        binding.amcLayout.root.isVisible = false
                        binding.progressBar.isVisible = true
                    }
                }
            }
        }


        commissioningViewModel.getWarrantyDetailsLiveData.observe(viewLifecycleOwner){
            if(!it.isResponseHandled()){
                when(it.status){
                    Status.SUCCESS ->{
                        it.data?.let { it1 -> updateWarrenty(it1) }
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }
        }





    }

    private fun updateWarrenty(data: WarrantyInfo) {
        when (data.status) {
            "Out of Warranty" -> {
                binding.warrantyInfoLayout.noWarrantyChip.visibility =  View.VISIBLE
                binding.warrantyInfoLayout.warrantyInfochip.visibility =  View.INVISIBLE
            }
            "NA" -> {
                binding.warrantyInfoLayout.noWarrantyChip.visibility = View.INVISIBLE
                binding.warrantyInfoLayout.warrantyInfochip.visibility =  View.VISIBLE
                binding.warrantyInfoLayout.warrantyInfochip.text = data.status
            }
            else -> {
                binding.warrantyInfoLayout.noWarrantyChip.visibility = View.INVISIBLE
                binding.warrantyInfoLayout.warrantyInfochip.visibility =  View.VISIBLE
            }
        }

        binding.warrantyInfoLayout.tvEndingOn.text = "End Date ${data.warranty_till}"
        binding.warrantyInfoLayout.tvStartedOn.text = "Start Date ${data.start_date}"
    }


    private fun updateAMCUi(data: ContractDetails?) {
        if(data?.status == "Yes"){
            binding.amcLayout.yesChipGroup.isVisible = true
        }else if(data?.status == "No"){
            binding.amcLayout.amclayoutChip.isVisible = true
        }
    }


    private fun api() {
        commissioningViewModel.getAmcDetails(args.serviceRequestId)
        commissioningViewModel.getWarrantyDetails(args.serviceRequestId)
    }


    private fun setupClickListeners() {

        binding.toolbarlayout.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.warrantyInfoLayout.root.setOnClickListener {
            val action = WarrantyAndOtherDetailsFragmentDirections.actionWarrantyAndOtherDetailsFragmentToWarrantyDetailsFragment(args.serviceRequestId)
            findNavController().navigate(action)
        }

        binding.extendedWarrantyLayout.root.setOnClickListener {
//            val action = WarrantyAndOtherDetailsFragmentDirections.actionWarrantyAndOtherDetailsFragmentToExtendedAndAMCDetailsFragment(false)
//            findNavController().navigate(action)
        }

        binding.amcLayout.root.setOnClickListener {
            val action = WarrantyAndOtherDetailsFragmentDirections.actionWarrantyAndOtherDetailsFragmentToExtendedAndAMCDetailsFragment(args.serviceRequestId,true)
            findNavController().navigate(action)
        }

        binding.omLayout.root.setOnClickListener {
            
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
