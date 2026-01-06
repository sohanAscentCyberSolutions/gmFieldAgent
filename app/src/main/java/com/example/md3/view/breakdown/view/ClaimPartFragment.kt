package com.example.md3.view.breakdown.view

import Status
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.md3.R
import com.example.md3.data.model.ClaimPart
import com.example.md3.data.model.ClaimPartSubmit
import com.example.md3.databinding.FragmentClaimPartBinding
import com.example.md3.utils.KotlinFunctions.addItemDecoration
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.ViewUtils
import com.example.md3.view.breakdown.adapters.ClaimPartParenAdapter
import com.example.md3.view.breakdown.adapters.OuterAdapter
import com.example.md3.view.breakdown.adapters.OuterData
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject

class ClaimPartFragment : Fragment() {

    private val TAG = "ClaimPartFragment"
    private lateinit var binding: FragmentClaimPartBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var outerAdapter: OuterAdapter
    private var claimPartParenAdapter = ClaimPartParenAdapter()
    private val args: ClaimPartFragmentArgs by navArgs()
    private val commissioningViewModel: CommissioningViewModel by inject()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClaimPartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        initToolbar()
        initUI()
        initRv()
        clickableViews()
        observe()




//
//        binding.recyclerView.layoutManager = LinearLayoutManager(context)
//        val listener = object : InnerAdapter.OnRemoveItemClickListener {
//            override fun onRemoveItemClick(outerPosition: Int, innerPosition: Int) {
//                onRemoveItemClickNew(outerPosition, innerPosition)
//            }
//        }
//        outerAdapter = OuterAdapter(getOuterData(), this, listener)
//        binding.recyclerView.adapter = outerAdapter
    }

    private fun initRv() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = claimPartParenAdapter
        binding.recyclerView.addItemDecoration(requireContext())
    }

    private fun observe() {

        commissioningViewModel.getAllMrnDetails(args.serviceRequestId, "")


        commissioningViewModel.mrnDetailsMutableLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            claimPartParenAdapter.submitList(it)
                        }
                    }

                    Status.ERROR -> {
                        claimPartParenAdapter.submitList(emptyList())
                    }

                    Status.LOADING -> {

                    }
                }
            }
        }


        sharedViewModel.mrnDetailsItemSelected.observe(viewLifecycleOwner) { selectedMrnDetailsItem ->
            if (selectedMrnDetailsItem != null) {
                val currentList = claimPartParenAdapter.currentList.toMutableList()
                val itemAlreadyExists = currentList.any { it.productDetails.id == selectedMrnDetailsItem.productDetails.id }
                if (!itemAlreadyExists) {
                    currentList.add(selectedMrnDetailsItem)
                    claimPartParenAdapter.submitList(currentList)
                    claimPartParenAdapter.notifyDataSetChanged()
                }
            }
        }



        commissioningViewModel.getClaimPartResponseLiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        ViewUtils.showSnackbar(binding.recyclerView,"Claim Part Submitted")
                        findNavController().popBackStack()
                        sharedViewModel.mrnDetailsItemSelected.postValue(null)
                    }
                    Status.ERROR -> {
                        ViewUtils.showSnackbar(binding.recyclerView,it.message)
                    }
                    Status.LOADING -> {

                    }
                }
            }
        }
    }


    private fun clickableViews() {
        binding.toolbar.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.btnRequestSetisCode.setOnClickListener {
//            val action = ClaimPartListFragmentDirections.actionGlobalClaimPartListFragment(
//                serviceRequestId = args.serviceRequestId,
//                isFromBreakdown = args.isFromBreakdown
//            )
//            findNavController().navigate(action)

            val dialogFragment = ClaimPartListFragment(args.serviceRequestId, args.isFromBreakdown)
            fragmentManager?.let { it1 -> dialogFragment.show(it1, "ClaimPartListFragment") }
        }



        binding.btnLayout.btnLogin.setOnClickListener {
            val claimPartSubmitList = mutableListOf<ClaimPartSubmit>()
            for (mrnDetailsItem in claimPartParenAdapter.currentList) {
                val productId = mrnDetailsItem.productDetails.id
                var qnty : Int = 0
                if(mrnDetailsItem.productDetails.inventoryType == "Normal"){
                    qnty = mrnDetailsItem.userEnteredQnty
                }else{
                    qnty = mrnDetailsItem.quantity
                }
                val identifiers = mrnDetailsItem.productDetails.identifier ?: emptyList()

                if(mrnDetailsItem.productDetails.inventoryType != "Normal"){
                    val claimPartSubmit = ClaimPartSubmit(
                        productId = productId,
                        quantity = qnty,
                        identifiers = identifiers
                    )
                    claimPartSubmitList.add(claimPartSubmit)
                }else{
                    val claimPartSubmit = ClaimPartSubmit(
                        productId = productId,
                        quantity = qnty,
                        identifiers = emptyList()
                    )
                    claimPartSubmitList.add(claimPartSubmit)

                }

            }


            val claimPart = ClaimPart(claimPartSubmitList)

            Log.d(TAG, "clickableViewsBefore: " + claimPartParenAdapter.currentList)
            Log.d(TAG, "clickableViewsAfter: " + claimPart)

            // Now you have a list of ClaimPartSubmit objects
            // For example, you can pass it to the ViewModel for further processing
            commissioningViewModel.submitClaimPart(args.serviceRequestId, claimPart)
        }


    }

    private fun initToolbar() {
        binding.toolbar.topAppBar.title = getString(R.string.claim_part)
    }

    private fun initUI() {
        binding.btnLayout.btnLogin.text = getString(R.string.confirm_claim)

    }

    fun onRemoveItemClickNew(outerPosition: Int, innerPosition: Int) {
        // Get the outer item at the specified position
        val outerData = outerAdapter.outerList[outerPosition]

        // Create a new list with the inner item removed
        val updatedInnerList = outerData.innerList.toMutableList()
        updatedInnerList.removeAt(innerPosition)

        // Create a new OuterData object with the updated inner list
        val updatedOuterData = OuterData(outerData.title, updatedInnerList)

        // Update the outerList with the new OuterData object
        val updatedOuterList = binding.recyclerView.adapter?.let { adapter ->
            (adapter as OuterAdapter).outerList.toMutableList().apply {
                set(outerPosition, updatedOuterData)
            }
        }

        // Set the updated outerList to the adapter
        updatedOuterList?.let {
            outerAdapter.outerList = it
            outerAdapter.notifyDataSetChanged()
        }
    }


    override fun onDestroy() {
        sharedViewModel.mrnDetailsItemSelected.postValue(null)
        super.onDestroy()
    }

}