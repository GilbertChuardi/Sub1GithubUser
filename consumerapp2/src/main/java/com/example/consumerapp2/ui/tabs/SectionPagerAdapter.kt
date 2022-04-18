package com.example.consumerapp2.ui.tabs

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.consumerapp2.R
import com.example.consumerapp2.ui.tabs.follower.FollowersFragment
import com.example.consumerapp2.ui.tabs.following.FollowingFragment

class SectionPagerAdapter(private val mContext: Context, mFragment: FragmentManager) :
    FragmentPagerAdapter(mFragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.followers, R.string.following)
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence =
        mContext.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = 2
}