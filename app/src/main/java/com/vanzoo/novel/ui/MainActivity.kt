package com.vanzoo.novel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vanzoo.novel.R
import com.vanzoo.novelsdk.ui.activity.NovelFragment

class MainActivity : AppCompatActivity() {
    private var mViewPager: ViewPager? = null
    private var mBottomNavigationView: BottomNavigationView? = null


    private val mFragments = mutableListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mViewPager = findViewById(R.id.viewPager)
        mBottomNavigationView = findViewById(R.id.bottom_navigation)

        mViewPager?.offscreenPageLimit = 2

        val novelFragment = NovelFragment()
        val homeFragment = HomeFragment()
        mFragments.add(novelFragment)
        mFragments.add(homeFragment)

        mViewPager?.offscreenPageLimit = 2
        mViewPager?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

        }

        mBottomNavigationView?.itemIconTintList = null
        mBottomNavigationView?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_novel -> mViewPager?.setCurrentItem(TAB_NOVEL, false)
                R.id.action_home -> mViewPager?.setCurrentItem(TAB_HOME, false)

            }
            true
        }
    }

    companion object {
        val TAB_NOVEL = 0
        val TAB_HOME = 1
    }

}