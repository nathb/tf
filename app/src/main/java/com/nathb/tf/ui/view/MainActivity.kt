package com.nathb.tf.ui.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.nathb.tf.R
import com.nathb.tf.ui.PageType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view_pager.adapter = pageAdapter
        view_pager.addOnPageChangeListener(pageChangeListener)
        view_pager.offscreenPageLimit = 3
        navigation.setOnNavigationItemSelectedListener(selectedItemListener)
        dynamic_fab.bringToFront()
    }

    private val pageAdapter = object : FragmentStatePagerAdapter(supportFragmentManager) {

        private val fragments : Array<Pair<Fragment, PageType>> = arrayOf(
                Pair(SearchFragment.newInstance(), PageType.SEARCH),
                Pair(ShowsFragment.newInstance(), PageType.SHOWS),
                Pair(BlankFragment.newInstance("C"), PageType.SETTINGS)
        )

        override fun getItem(position: Int): Fragment {
            return fragments[position].first
        }

        override fun getCount(): Int {
            return fragments.size
        }

        fun getPageType(position: Int): PageType {
            return fragments[position].second
        }
    }

    private val selectedItemListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_search -> {
                view_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_shows -> {
                view_pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                view_pager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val pageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {

        var selectedPosition = 0

        override fun onPageSelected(position: Int) {
            if (position != selectedPosition) {
                navigation.menu.getItem(selectedPosition).isChecked = false
                navigation.menu.getItem(position).isChecked = true
                selectedPosition = position
                dynamic_fab.setPage(pageAdapter.getPageType(position))
            }
        }
    }
}
