package com.example.md3.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.md3.view.commissioning.view.CasesFragment

const val COMMISSIONING_ID = "commissioning_id"
class GenericViewPagerAdapter<T : Fragment>(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentTitleList: List<String>,
    private val fragments: List<Class<out T>>,
    private val tabTypes: List<TabType>,
    private val parameters : String?  = null

) : FragmentStateAdapter(fm, lifecycle) {


    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        val fragmentClass = fragments[position]
        val tabType = tabTypes[position]
        return fragmentClass.newInstance().apply {
            arguments = Bundle().apply {
                putSerializable(CasesFragment.ARG_TAB_TYPE, tabType)
                putString(COMMISSIONING_ID , parameters)
            }
        }
    }


    fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }
}

