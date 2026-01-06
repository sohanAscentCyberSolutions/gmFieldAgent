package com.example.md3.view.breakdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings
import com.example.md3.databinding.FragmentWarrantyDetailsBinding
import com.example.md3.utils.GenericViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class WarrantyDetailsFragment : Fragment() {

    private var _binding: FragmentWarrantyDetailsBinding? = null
    private val args : WarrantyDetailsFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: GenericViewPagerAdapter<Fragment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWarrantyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar()
        setAdapter(args.serviceRequestId)
    }

    private fun toolbar() {
        binding.toolbarlayout.endTitleTextView.text = "Warranty Details"
    }



    fun setAdapter(id: String) {
        val fragmentTitles = listOf("Part Wise Warranty", "Warranty Consumed")
        val fragmentClasses = listOf(
            WarrantyTypesFragment::class.java,
            WarrantyTypesFragment::class.java
        )

        viewPagerAdapter = GenericViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            fragmentTitles,
            fragmentClasses,
            ConstantStrings.WARRANTY_TAB_TYPES,
            id
        )

        with(binding.warrantyDetailsViewPager) {
            adapter = viewPagerAdapter
            offscreenPageLimit = androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        }

        TabLayoutMediator(binding.toolbarlayout.tabLayout, binding.warrantyDetailsViewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()


        binding.toolbarlayout.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }


    private fun addTabItem(title: String) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
