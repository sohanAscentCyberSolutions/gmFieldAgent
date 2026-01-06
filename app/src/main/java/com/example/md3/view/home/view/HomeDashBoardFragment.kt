package com.example.md3.view.home.view

import Status
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodii.adapter.ReuseAdapter
import com.example.md3.MainActivity
import com.example.md3.R
import com.example.md3.Urls
import com.example.md3.data.model.home.HomeScreenKPI
import com.example.md3.data.model.home_page.HomePageResults
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.FragmentHomePageBinding
import com.example.md3.utils.AuthUtils
import com.example.md3.utils.TimePickerUtils
import com.example.md3.utils.glide.GlideImageLoader
import com.example.md3.view.auth.AuthActivity
import com.example.md3.view.commissioning.CommissioningViewModel
import org.koin.android.ext.android.inject


class HomeDashBoardFragment : Fragment(R.layout.fragment_home_page) {

    private var _binding: FragmentHomePageBinding? = null
    private val sharedPrefs: SharedPrefs by inject()
    private val commissioningViewModel: CommissioningViewModel by inject()
    private val binding get() = _binding!!
    private val TAG = "HomePageFragment"

    private lateinit var newHomeMenuAdapter: ReuseAdapter<HomePageResults>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initClicks()
        apiCall()
        observe()
        binding.toolbarlayout.endTitleTextView.text = sharedPrefs.organisationUserName
    }

    private fun observe() {
        commissioningViewModel.getHomeKPILiveData.observe(viewLifecycleOwner) {
            if (!it.isResponseHandled()) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { it1 ->
                            updateUi(it1)
                        }
                    }

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }
                }
            }
        }
    }

    private fun updateUi(it1: HomeScreenKPI) {
        binding.casesStaticsView?.apply {
            tvNewCases.text = it1.combinedDetails.newCase.toString()
            tvAcceptedCases.text = it1.combinedDetails.acceptedCase.toString()
            tvRejectedCases.text = it1.combinedDetails.rejectedCase.toString()
            tvClosedCases.text = it1.combinedDetails.closedCase.toString()
        }

        binding.visitStaticsLayout?.apply {
            tvNewCases.text = it1.combinedDetails.upcomingVisits.toString()
            tvAcceptedCases.text = it1.combinedDetails.completedVisits.toString()
        }

        binding.totalWorkTimeValue.text = it1.combinedDetails.totalWorkTimeString.toString()
        binding.totaljourneyTime.text = it1.combinedDetails.totalJourneyTimeString.toString()


        binding.tvnewCases.text = it1.commissioningDetails.newCase.toString() + " New"
        binding.tvOpenCases.text = it1.commissioningDetails.upcomingVisits.toString() + " Open"
        binding.tvVisitCases.text = it1.commissioningDetails.totalVisits.toString() + " Visit"


        binding.tvnewCasesBreakdown.text = it1.breakdownDetails.newCase.toString() + " New"
        binding.tvOpenCasesBreakdown.text = it1.breakdownDetails.upcomingVisits.toString() + " Open"
        binding.tvVisitCasesBreakdown.text = it1.breakdownDetails.totalVisits.toString() + " Visit"

    }

    private fun apiCall() {
        commissioningViewModel.getHomeKPI("")
    }


    private fun initClicks() {
        binding.apply {

            toolbarlayout.toolbar.setNavigationOnClickListener {
                (requireActivity() as MainActivity).openDrawer()
            }

            cardCommissioning.setOnClickListener {
//                val filteredMap: MutableMap<String?, Any>? = HashMap()
//                val dataMap = mapOf(NOTIFICATION_TYPE to "commissioning_new_case" , NOTIFICATION_CASE_ID to "f83fdce8-19f2-4f8a-a5bd-28d91d6dca82" , NOTIFICATION_CASE_FORMATTED_ID to "#test")
//                Log.d(TAG, "onMessageReceivedDataMap: $dataMap")
//                for ((key, value) in dataMap) {
//                    if (value != null) {
//                        filteredMap?.set(key, value)
//                    }
//                }
//                Log.d(TAG, "initClicks: " + filteredMap)
//                NotificationUtils.sendNotification(requireContext(),"test" , "test" , filteredMap)
                findNavController().navigate(R.id.action_homePageFragment_to_homeFragment)
            }

            cardBreakdown.setOnClickListener {
                findNavController().navigate(R.id.action_homePageBreakdownFragment_to_homeFragment)
            }


            tvDateRange.setOnClickListener {
                TimePickerUtils.showDateRangePicker(requireContext(),
                    object : TimePickerUtils.DateRangePickerListener {
                        override fun onSelected(
                            startDate: String,
                            endDate: String,
                            formattedStartDateForBackend: String,
                            formattedEndDateForBackend: String
                        ) {
                            binding.tvDateRange.text = "$startDate to $endDate"
                            commissioningViewModel.getHomeKPI(formattedStartDateForBackend)
                        }

                    }
                )
            }

            btnLogin.setOnClickListener {
                AuthUtils.clearAuthDate()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
        }
    }


    private fun initToolbar() {

        binding.toolbarlayout.apply {
            GlideImageLoader(requireContext()).loadImage(
                Urls.BASE_URL + sharedPrefs.logo,
                binding.toolbarlayout.startImageView
            )
            binding.toolbarlayout.endTitleTextView.text = sharedPrefs.organisationUserName

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}