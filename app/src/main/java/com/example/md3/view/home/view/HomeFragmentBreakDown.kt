package com.example.md3.view.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings
import com.example.md3.R
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.databinding.FragmentHomeBinding
import com.example.md3.databinding.FragmentHomeBreakdownBinding
import com.example.md3.utils.GenericViewPagerAdapter
import com.example.md3.utils.SharedViewModel
import com.example.md3.utils.TabType
import com.example.md3.view.breakdown.view.BreakdownCasesFragment
import com.example.md3.view.commissioning.view.CasesFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject


class HomeFragmentBreakDown : Fragment(R.layout.fragment_home_breakdown) {

    private var _binding: FragmentHomeBreakdownBinding? = null
    private val sharedPrefs: SharedPrefs by inject()
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var viewPagerAdapter: GenericViewPagerAdapter<Fragment>
    private val binding get() = _binding!!
    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBreakdownBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        apiCall()
        initToolbar()
        observe()
        setAdapter()
    }

    private fun apiCall() {

    }

    private fun initViews() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    private fun initToolbar() {
        binding.toolbarlayout.apply {
            binding.toolbarlayout.toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            binding.toolbarlayout.endTitleTextView.text = "BreakDown"
        }
    }

    fun setAdapter() {
        val fragmentTitles = ConstantStrings.TAB_TITLES
        val fragmentClasses = listOf(
            BreakdownCasesFragment::class.java,
            BreakdownCasesFragment::class.java,
            BreakdownCasesFragment::class.java
        )

        viewPagerAdapter = GenericViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            fragmentTitles,
            fragmentClasses,
            ConstantStrings.TAB_TYPES
        )

        with(binding.homeViewPager) {
            adapter = viewPagerAdapter
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        }

        TabLayoutMediator(binding.toolbarlayout.tabLayout, binding.homeViewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()


    }


    private fun observe() {
        sharedViewModel.tabChangeLiveData.observe(viewLifecycleOwner){
            if(it == TabType.ALL_CASES){
                binding.homeViewPager.setCurrentItem(1,false)
            }
            sharedViewModel.tabChangeLiveData.postValue(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}