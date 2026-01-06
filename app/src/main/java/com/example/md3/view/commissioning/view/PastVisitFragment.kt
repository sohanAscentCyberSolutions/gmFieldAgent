package com.example.md3.view.commissioning.view

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
import com.example.md3.data.model.commissioning.visit.VisitResult
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.FragmentPastVistBinding
import com.example.md3.databinding.PastVisitLayoutBinding
import com.example.md3.utils.COMMISSIONING_ID
import com.example.md3.utils.DismissBottomSheet
import com.example.md3.utils.GenericPagingAdapter
import com.example.md3.utils.KotlinFunctions
import com.example.md3.utils.TabType
import com.example.md3.utils.ViewUtils
import com.example.md3.view.commissioning.CommissioningViewModel
import com.example.md3.view.visit.VisitInputDetailsBottomSheet
import org.koin.android.ext.android.inject


class PastVisitFragment : Fragment() {

    private var _binding: FragmentPastVistBinding? = null
    private lateinit var pastVisitAdapter: GenericPagingAdapter<VisitResult>
    private lateinit var myVisitAdapter: GenericPagingAdapter<VisitResult>
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
        _binding = FragmentPastVistBinding.inflate(inflater, container, false)
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
            TabType.MY_VISITS -> {
                commissioningID?.let {
                    commissioningViewModel.getMyVisits(
                        sharedPrefs.organisationUserId,
                        it
                    )
                }
            }

            TabType.PAST_VISIT -> {
                commissioningID?.let { commissioningViewModel.getAllVisits(commissioningId = it) }
            }

            else -> {}
        }
    }


    private fun clickableViews() {
        binding.pastVisitLayout.swipRefreshLayout.setOnRefreshListener {
            when (tabType) {
                TabType.MY_VISITS -> {
                    myVisitAdapter.refresh()
                }

                TabType.PAST_VISIT -> {
                    pastVisitAdapter.refresh()
                }

                else -> {}
            }
            binding.pastVisitLayout.swipRefreshLayout.isRefreshing = false
        }

        binding.pastVisitLayout.buttonRetry.setOnClickListener {
            when (tabType) {
                TabType.MY_VISITS -> {
                    myVisitAdapter.retry()
                }

                TabType.PAST_VISIT -> {
                    pastVisitAdapter.retry()
                }

                else -> {

                }
            }
        }
    }


    private fun observe() {


        commissioningViewModel.getVisitDeleteLiveData.observe(viewLifecycleOwner){
            if(!it.isResponseHandled()){
                when(it.status){
                    Status.SUCCESS -> {
                        pastVisitAdapter.refresh()
                        myVisitAdapter.refresh()
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }
        }






        when (tabType) {
            TabType.MY_VISITS -> {
                binding.pastVisitLayout.recyclerView.adapter = myVisitAdapter
                commissioningViewModel.getMyVisit?.observe(viewLifecycleOwner) {
                    myVisitAdapter.submitData(lifecycle, it)
                }
            }

            TabType.PAST_VISIT -> {
                binding.pastVisitLayout.recyclerView.adapter = pastVisitAdapter
                commissioningViewModel.getAllPastVisit?.observe(viewLifecycleOwner) {
                    pastVisitAdapter.submitData(lifecycle, it)
                }
            }

            else -> {}
        }

    }


    private fun initRecyclerview() {
        binding.pastVisitLayout.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun initAdapter() {
        pastVisitAdapter = GenericPagingAdapter(
            R.layout.past_visit_layout,
            { view, visitResult , position ->
                val binding = PastVisitLayoutBinding.bind(view)
                binding.tvVisitNumber.text = "Visit #" + visitResult.visitCount.toString()
                binding.tvname.text = visitResult.assignedEngineerDetails?.name ?: "--------"
                binding.tvVisitSummeryValue.text = visitResult.summary ?: "------"
                binding.tvStatus.text = visitResult.status
                binding.tvStatus.setChipBackgroundColorResource(
                    KotlinFunctions.getCaseStatusColor(
                        visitResult.status
                    )
                )

                binding.tvViewDetails.setOnClickListener {
//                    val bundle = Bundle()
//                    bundle.putString(VisitInputDetailsBottomSheet.VISIT_ID, visitResult.id)
//                    val bottomSheet = VisitInputDetailsBottomSheet()
//                    bottomSheet.arguments = bundle
//                    bottomSheet.show(childFragmentManager, "")

                    if (visitResult.status == "Completed" || visitResult.status == "Continued") {
                        val bundle = Bundle()
                        bundle.putString(VisitInputDetailsBottomSheet.VISIT_ID, visitResult.id)
                        val bottomSheet = VisitInputDetailsBottomSheet()
                        bottomSheet.arguments = bundle
                        bottomSheet.show(childFragmentManager, "")
                    } else {
                        val action = ViewOpenCasesFragmentDirections.actionViewOpenCasesFragmentToCurrentVisitFragment(visitResult.id, "Visit # " + visitResult.visitCount.toString())
                        findNavController().navigate(action)
                    }
                }
            },
            { item ->

            }
        )

        pastVisitAdapter.addLoadStateListener { loadStates ->

            val refreshState = loadStates.source.refresh
            binding.pastVisitLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading

            when (refreshState) {
                is LoadState.Loading -> {
                    binding.pastVisitLayout.progressBar.isVisible = true
                    binding.pastVisitLayout.recyclerView.isVisible = false
                    binding.pastVisitLayout.tvEmptyTitle.isVisible = false
                }

                is LoadState.NotLoading -> {
                    if (pastVisitAdapter.itemCount == 0) {
                        binding.pastVisitLayout.progressBar.isVisible = false
                        binding.pastVisitLayout.recyclerView.isVisible = false
                        binding.pastVisitLayout.tvEmptyTitle.isVisible = true
                        binding.pastVisitLayout.tvEmptyTitle.text =
                            getString(R.string.no_visit_found)
                    } else {
                        binding.pastVisitLayout.progressBar.isVisible = false
                        binding.pastVisitLayout.recyclerView.isVisible = true
                        binding.pastVisitLayout.tvEmptyTitle.isVisible = true
                    }
                }

                is LoadState.Error -> {
                    binding.pastVisitLayout.progressBar.isVisible = false
                    binding.pastVisitLayout.recyclerView.isVisible = false
                    binding.pastVisitLayout.tvEmptyTitle.isVisible = true
                    binding.pastVisitLayout.buttonRetry.isVisible = true
                    binding.pastVisitLayout.tvEmptyTitle.text =
                        getString(R.string.something_went_wrong)
                }

            }
        }




        myVisitAdapter = GenericPagingAdapter(
            R.layout.past_visit_layout,
            { view, visitResult, position ->
                val binding = PastVisitLayoutBinding.bind(view)
                binding.tvVisitNumber.text = "Visit #" + visitResult.visitCount.toString()
                binding.tvname.text = visitResult.assignedEngineerDetails?.name ?: "--------"
                binding.tvVisitSummeryValue.text = visitResult.summary ?: "------"
                binding.tvStatus.text = visitResult.status
                binding.tvStatus.setChipBackgroundColorResource(
                    KotlinFunctions.getCaseStatusColor(
                        visitResult.status
                    )
                )


                if(visitResult.status == "Completed" || visitResult.status == "Continued" || visitResult.status == "Schedule"){
                    //close
                    binding.delete.isVisible = false
                }else{
                    // open
                    binding.delete.isVisible = true
                }




                binding.tvViewDetails.setOnClickListener {

                    if (visitResult.status == "Completed" || visitResult.status == "Continued" ) {
                        val bundle = Bundle()
                        bundle.putString(VisitInputDetailsBottomSheet.VISIT_ID, visitResult.id)
                        val bottomSheet = VisitInputDetailsBottomSheet()
                        bottomSheet.arguments = bundle
                        bottomSheet.show(childFragmentManager, "")
                    } else {
                        val action = ViewOpenCasesFragmentDirections.actionViewOpenCasesFragmentToCurrentVisitFragment(visitResult.id, "Visit # " + visitResult.visitCount.toString())
                        findNavController().navigate(action)
                    }

                }


                binding.delete.setOnClickListener {

                    if(visitResult.assignedEngineerDetails?.id == sharedPrefs.organisationUserId){
                        val dismissBottomSheet = DismissBottomSheet(false){
                            if(it){
                                commissioningViewModel.deleteVisit(visitResult.id)
                            }
                        }
                        dismissBottomSheet.show(childFragmentManager,"")
                    }else{
                        ViewUtils.showSnackbar(
                            binding.root,
                            "Can't not delete someone else visit"
                        )
                    }
                }

            },
            { item ->


            }
        )


        myVisitAdapter.addLoadStateListener { loadStates ->

            val refreshState = loadStates.source.refresh
            binding.pastVisitLayout.recyclerView.isVisible = refreshState is LoadState.NotLoading

            when (refreshState) {
                is LoadState.Loading -> {
                    binding.pastVisitLayout.progressBar.isVisible = true
                    binding.pastVisitLayout.recyclerView.isVisible = false
                    binding.pastVisitLayout.tvEmptyTitle.isVisible = false
                }

                is LoadState.NotLoading -> {
                    if (myVisitAdapter.itemCount == 0) {
                        binding.pastVisitLayout.progressBar.isVisible = false
                        binding.pastVisitLayout.recyclerView.isVisible = false
                        binding.pastVisitLayout.tvEmptyTitle.isVisible = true
                        binding.pastVisitLayout.tvEmptyTitle.text =
                            getString(R.string.no_visit_found)
                    } else {
                        binding.pastVisitLayout.progressBar.isVisible = false
                        binding.pastVisitLayout.recyclerView.isVisible = true
                        binding.pastVisitLayout.tvEmptyTitle.isVisible = true
                    }
                }

                is LoadState.Error -> {
                    binding.pastVisitLayout.progressBar.isVisible = false
                    binding.pastVisitLayout.recyclerView.isVisible = false
                    binding.pastVisitLayout.tvEmptyTitle.isVisible = true
                    binding.pastVisitLayout.buttonRetry.isVisible = true
                    binding.pastVisitLayout.tvEmptyTitle.text =
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