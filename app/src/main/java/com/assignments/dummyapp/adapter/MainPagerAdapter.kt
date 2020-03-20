package com.assignments.dummyapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.assignments.dummyapp.view.NewsListFragment
import com.assignments.dummyapp.view.SavedNewsFragment

class MainPagerAdapter(fragmentManager: FragmentManager):
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        return if (position == 0) NewsListFragment() else SavedNewsFragment()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "News" else "Saved"
    }
}