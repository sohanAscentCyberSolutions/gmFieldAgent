package com.example.md3.view.breakdown.view

import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.md3.data.model.amc.Contract
import com.example.md3.data.model.amc.ContractDetails
import com.example.md3.data.model.amc.LinkedContractDetails
import com.example.md3.data.model.amc.Part
import com.example.md3.data.model.amc.Service
import com.example.md3.databinding.ContractItemBinding
import com.example.md3.databinding.ContractLayoutBinding
import com.example.md3.databinding.FragmentExtendedAndAMCDetailsBinding
import com.example.md3.databinding.NoofpartsItemLayoutBinding
import com.example.md3.utils.CommonAdapter
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class ExtendedAndAMCDetailsFragment : Fragment() {
    private lateinit var mrnCommonAdapter: CommonAdapter
    private lateinit var contractAdapter: CommonAdapter
    private lateinit var serviceAdapter: CommonAdapter
    private var _binding: FragmentExtendedAndAMCDetailsBinding? = null
    private val args: ExtendedAndAMCDetailsFragmentArgs by navArgs()
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtendedAndAMCDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarlayout.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        if (args.isAmcType) {
            binding.toolbarlayout.topAppBar.title = "AMC Details"
        } else {
            binding.toolbarlayout.topAppBar.title = "Extended Warranty Details"
        }
        api()
        setupViews()
        initAdapters()
        clickableVies()
        observe()
    }


    private fun observe() {
        commissioningViewModel.getAmcDetailsLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.isVisible = false
                        binding.scrollView.isVisible = true
                        updateAMCUi(it.data)
                    }

                    Status.ERROR -> {
                        binding.progressBar.isVisible = false
                        binding.scrollView.isVisible = false
                    }

                    Status.LOADING -> {
                        binding.progressBar.isVisible = true
                        binding.scrollView.isVisible = false
                    }
                }
            }
        }


    }

    private fun updateAMCUi(data: ContractDetails?) {
        binding.tvAmcStatusValue.text = data?.status
        binding.tvWarrantyEndDateValue.text = data?.applicable
        binding.tvAMCPriceValue.text = ""


        if (data?.status == "No" && data.applicable == "No") {
            binding.tvAMCPrice.isVisible = false
            binding.tvAMCPriceValue.isVisible = false
            binding.scrollView.isVisible = true
            binding.contractRecyclerview.isVisible = false
            binding.contractName.root.isVisible = false
        } else if (data?.status == "No" && data.applicable == "Yes") {
            binding.tvAMCPrice.isVisible = false
            updateUI(data.contracts)
        } else if (data?.status == "Yes") {
            updateUIForAvaiableContract(data.linked_contract_details)
            binding.tvWarrantyEndDateValue.isVisible = false
            binding.tvWarrantyEndDate.isVisible = false
        } else {

        }

    }

    private fun updateUIForAvaiableContract(linkedContractDetails: LinkedContractDetails?) {
        binding.contractRecyclerview.isVisible = false
        binding.tvAvaiable.isVisible = true
        binding.tvAvaiable.text = "Contracts Details"
        linkedContractDetails?.let { setValuesInViews(it) }
    }


    fun setValuesInViews(
        contractDetails: LinkedContractDetails
    ) {
        binding.contractName.contractLayout.apply {

            binding.tvAMCPriceValue.text = contractDetails.totalCost.toString()


            tvAMCPrice.text = "Duration"
            tvAMCPriceValue.text = contractDetails.duration.toString() + " Month's"


            tvDuration.text = "Started On"
            tvDurationValue.text = contractDetails.startDate




            tvOnSite.text = "Ending On"
            tvOnSiteValue.text = contractDetails.endDate


            onSiteService.text = "OnSite Service"
            onSiteServiceValue.text = if (contractDetails.flagOnsiteService) "Yes" else "No"


            productAgeValue.text = contractDetails.productAge.toString()


            binding.contractName.serviceRecyclerView.apply {
                adapter = serviceAdapter
                serviceAdapter.submitList(contractDetails.services)
            }


            binding.contractName.noofpartsLayout.apply {
                adapter = mrnCommonAdapter
                mrnCommonAdapter.submitList(contractDetails.parts)
            }





//            tvIssueTitle.text = contractDetails.name
//            tvAMCPriceValue.text =  contractDetails.total_cost.toString()
//            tvDurationValue.text = item.duration.toString() + "Months"
//            tvOnSiteValue.text = if(item.flag_onsite_service) "Yes" else "No"
        }
    }


    private fun updateUI(contracts: List<Contract>) {
        binding.tvAvaiable.isVisible = true
        contractAdapter.submitList(contracts)
    }


    private fun initAdapters() {

        mrnCommonAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                NoofpartsItemLayoutBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is NoofpartsItemLayoutBinding && item is Part) {
                    binding.tvOnSite.text = item.product_details.name
                    binding.tvOnSiteValue.text = item.max_replacement_allowed.toString()
                }
            }
        )


        serviceAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                ContractItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is ContractItemBinding && item is Service) {
                    binding.tvIssueTitle.text = item.service_type
                    binding.tvAMCPrice.text = "Duration"
                    binding.tvAMCPriceValue.text = item.duration.toString() + " Months"
                    binding.tvDuration.text = "Start Date"
                    binding.tvDurationValue.text = "---"
                    binding.tvOnSite.text = "Free Services"
                    binding.tvOnSiteValue.text = item.free_service_count.toString()
                }
            }
        )




        contractAdapter = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                ContractLayoutBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is ContractLayoutBinding && item is Contract) {


                    binding.contractLayout.apply {
                        tvIssueTitle.text = item.name
                        tvAMCPriceValue.text = item.total_cost.toString()
                        tvDurationValue.text = item.duration.toString() + "Months"
                        tvOnSiteValue.text = if (item.flag_onsite_service) "Yes" else "No"
                    }


                    binding.serviceRecyclerView.apply {
                        adapter = serviceAdapter
                        serviceAdapter.submitList(item.services)
                    }


                    binding.noofpartsLayout.apply {
                        adapter = mrnCommonAdapter
                        mrnCommonAdapter.submitList(item.parts)
                    }



                    item?.category_details?.let {
                        binding.categoryLayout.root.isVisible = true
                        binding.categoryLayout.apply {
                            divider1.isVisible = false
                            tvIssueTitle.text = "Category"
                            tvAMCPrice.text = item.category_details.name ?: "---"
                            tvAMCPriceValue.text = "----"
                            tvDuration.isVisible = false
                            tvDurationValue.isVisible = false
                            tvOnSite.isVisible = false
                            tvOnSiteValue.isVisible = false
                        }
                    }


                }
            }
        )


        binding.contractRecyclerview.adapter = contractAdapter
    }

    private fun api() {
        commissioningViewModel.getAmcDetails(args.serviceRequestId)
    }

    private fun clickableVies() {


        binding.btnDone.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setupViews() {
        setTextViewValues()
        setTextViewVisibility()
    }

    private fun setTextViewValues() {
        binding.tvAmcStatusValue.text = "Yes" // Set AMC Status value
        binding.tvWarrantyEndDateValue.text = "Yes" // Set Warranty End Date value
        binding.tvAMCPriceValue.text = "1999000" // Set AMC Price value
    }

    private fun setTextViewVisibility() {
        val isAmcAvailable = true // Replace with your condition for AMC availability
        val isWarrantyAvailable = true // Replace with your condition for Warranty availability

        binding.tvAmcStatus.visibility = if (isAmcAvailable) View.VISIBLE else View.GONE
        binding.tvAmcStatusValue.visibility = if (isAmcAvailable) View.VISIBLE else View.GONE
        binding.tvWarrantyEndDate.visibility = if (isWarrantyAvailable) View.VISIBLE else View.GONE
        binding.tvWarrantyEndDateValue.visibility =
            if (isWarrantyAvailable) View.VISIBLE else View.GONE
        binding.tvAMCPrice.visibility = if (isAmcAvailable) View.VISIBLE else View.GONE
        binding.tvAMCPriceValue.visibility = if (isAmcAvailable) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
