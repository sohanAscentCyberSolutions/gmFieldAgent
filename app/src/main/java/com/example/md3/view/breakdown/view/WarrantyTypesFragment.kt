package com.example.md3.view.breakdown.view

import Status
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.breakdown.WarrantyConsumedResult
import com.example.md3.data.model.mrn.PartResultItem
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.FragmentWarrentyTypesBinding
import com.example.md3.databinding.WarrantyDateListItemBinding
import com.example.md3.databinding.WarrantyItemLayoutBinding
import com.example.md3.utils.COMMISSIONING_ID
import com.example.md3.utils.CommonAdapter
import com.example.md3.utils.GenericPagingAdapter
import com.example.md3.utils.TabType
import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.commissioning.view.CasesFragment
import org.koin.android.ext.android.inject


class WarrantyTypesFragment : Fragment() {

    private var _binding: FragmentWarrentyTypesBinding? = null
//    private lateinit var partyWiseWarranty: GenericPagingAdapter<PartWiseWarrantyResult>
    private lateinit var partyWiseWarranty: CommonAdapter
    private lateinit var warrantyConsumed: GenericPagingAdapter<WarrantyConsumedResult>
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val sharedPrefs: SharedPrefs by inject()
    private val binding get() = _binding!!
    private lateinit var tabType: TabType
    private var commissioningID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabType = it.getSerializable(CasesFragment.ARG_TAB_TYPE) as TabType
            commissioningID = it.getString(COMMISSIONING_ID) as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWarrentyTypesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        apiCall()
        initRecyclerview()
        observe()
        clickableViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun apiCall() {
        when (tabType) {
            TabType.PART_WISE_WARRANTY -> {
                commissioningID?.let { commissioningViewModel.getAllPartList(it, "") }
            }

            TabType.WARRANTY_CONSUMED -> {
                commissioningViewModel.getAllWarrantyConsumed("")
            }

            else -> {}
        }
    }


    private fun clickableViews() {

        binding.btnDone.setOnClickListener {
            findNavController().popBackStack()
        }



        binding.recyclerviewLayout.swipRefreshLayout.setOnRefreshListener {
            when (tabType) {
                TabType.MY_VISITS -> {
                    warrantyConsumed.refresh()
                }

                TabType.PAST_VISIT -> {
//                    partyWiseWarranty.refresh()
                }

                else -> {}
            }
            binding.recyclerviewLayout.swipRefreshLayout.isRefreshing = false
        }

        binding.recyclerviewLayout.buttonRetry.setOnClickListener {
            when (tabType) {
                TabType.MY_VISITS -> {
                    warrantyConsumed.retry()
                }

                TabType.PAST_VISIT -> {
//                    partyWiseWarranty.retry()
                }

                else -> {

                }
            }
        }
    }


    private fun observe() {
        when (tabType) {
            TabType.PART_WISE_WARRANTY -> {
                binding.recyclerviewLayout.swipRefreshLayout.isEnabled = false
                binding.recyclerviewLayout.recyclerView.adapter = partyWiseWarranty

//                commissioningViewModel.getAllPartWiseWarranty?.observe(viewLifecycleOwner) {
//                    partyWiseWarranty.submitData(lifecycle, it)
//                }


                commissioningViewModel.getPartsMutableLiveData.observe(viewLifecycleOwner) {
                    if (!it.isResponseHandled()) {
                        when (it.status) {
                            Status.SUCCESS -> {
                                it.data?.let {
                                    partyWiseWarranty.submitList(it)
                                }
                            }

                            Status.ERROR -> {
                                partyWiseWarranty.submitList(emptyList())
                            }

                            Status.LOADING -> {

                            }
                        }
                    }
                }


            }

            TabType.WARRANTY_CONSUMED -> {
                binding.recyclerviewLayout.recyclerView.adapter = warrantyConsumed
                commissioningViewModel.getAllWarrantyConsumed?.observe(viewLifecycleOwner) {
                    warrantyConsumed.submitData(lifecycle, it)
                }
            }

            else -> {}
        }
    }



    private fun initRecyclerview() {
        binding.recyclerviewLayout.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun initAdapter() {
//        partyWiseWarranty = GenericPagingAdapter(
//            R.layout.warranty_date_list_item,
//            { view, visitResult , position ->
//                val binding = WarrantyDateListItemBinding.bind(view)
//                binding.titleTv.text = visitResult.partName
//                binding.titleNumber.text = visitResult.partNumber
//                binding.tvWarrantyStartDateValue.text = visitResult.warrantyStatusStartDate
//                binding.tvWarrantyEndDateValue.text = visitResult.warrantyStatusEndDate
//            },
//            { item ->
//
//            }
//        )


        partyWiseWarranty = CommonAdapter(
            inflate = { layoutInflater, parent, attachToParent ->
                WarrantyDateListItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            onBind = { binding, item, position ->
                if (binding is WarrantyDateListItemBinding && item is PartResultItem) {
                    binding.titleTv.text = item.bomItemDetails.name
                    binding.titleNumber.text = item.bomItemDetails.partNumber
                    binding.tvWarrantyStartDateValue.text = item.warrantyDetails.startDate
                    binding.tvWarrantyEndDateValue.text = item.warrantyDetails.warrantyTill


                    if (item.bomItemDetails.warranty != 0) {
                        binding.warrantyInfochip.visibility = View.VISIBLE
                        binding.chipOutWarranty.visibility = View.INVISIBLE
                    } else {
                        binding.warrantyInfochip.visibility = View.INVISIBLE
                        binding.chipOutWarranty.visibility = View.VISIBLE
                    }
                }
            }
        )






        warrantyConsumed = GenericPagingAdapter(
            R.layout.warranty_item_layout,
            { view, visitResult, position ->
                val binding = WarrantyItemLayoutBinding.bind(view)
                binding.customerNameValue.text = visitResult.id
                binding.totalWorkTimeValue.text =  visitResult.serviceType
                binding.totalJourneyTimeValue.text = visitResult.product
                binding.totalVisitsValue.text = visitResult.partClaimed
                binding.customerRatingValue.text = visitResult.partNumber
            },
            { item ->


            }
        )



//        partyWiseWarranty.addLoadStateListener { loadStates ->
//
//            val refreshState = loadStates.source.refresh
//            binding.recyclerviewLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading
//
//            when (refreshState) {
//                is LoadState.Loading -> {
//                    binding.recyclerviewLayout.progressBar.isVisible = true
//                    binding.recyclerviewLayout.recyclerView.isVisible = false
//                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
//                }
//
//                is LoadState.NotLoading -> {
//                    if (partyWiseWarranty.itemCount == 0) {
//                        binding.recyclerviewLayout.progressBar.isVisible = false
//                        binding.recyclerviewLayout.recyclerView.isVisible = false
//                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
//                        binding.recyclerviewLayout.tvEmptyTitle.text =
//                            getString(R.string.no_visit_found)
//                    } else {
//                        binding.recyclerviewLayout.progressBar.isVisible = false
//                        binding.recyclerviewLayout.recyclerView.isVisible = true
//                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
//                    }
//                }
//
//                is LoadState.Error -> {
//                    binding.recyclerviewLayout.progressBar.isVisible = false
//                    binding.recyclerviewLayout.recyclerView.isVisible = false
//                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
//                    binding.recyclerviewLayout.buttonRetry.isVisible = true
//                    binding.recyclerviewLayout.tvEmptyTitle.text =
//                        getString(R.string.something_went_wrong)
//                }
//
//            }
//        }




        warrantyConsumed.addLoadStateListener { loadStates ->

            val refreshState = loadStates.source.refresh
            binding.recyclerviewLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading

            when (refreshState) {
                is LoadState.Loading -> {
                    binding.recyclerviewLayout.progressBar.isVisible = true
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = false
                }

                is LoadState.NotLoading -> {
                    if (warrantyConsumed.itemCount == 0) {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = false
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                        binding.recyclerviewLayout.tvEmptyTitle.text =
                            getString(R.string.no_visit_found)
                    } else {
                        binding.recyclerviewLayout.progressBar.isVisible = false
                        binding.recyclerviewLayout.recyclerView.isVisible = true
                        binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                    }
                }

                is LoadState.Error -> {
                    binding.recyclerviewLayout.progressBar.isVisible = false
                    binding.recyclerviewLayout.recyclerView.isVisible = false
                    binding.recyclerviewLayout.tvEmptyTitle.isVisible = true
                    binding.recyclerviewLayout.buttonRetry.isVisible = true
                    binding.recyclerviewLayout.tvEmptyTitle.text =
                        getString(R.string.something_went_wrong)
                }

            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}