package com.vanzoo.novel.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vanzoo.novel.R
import com.vanzoo.novel.utils.ScreenUtils
import com.vanzoo.novel.utils.StatusBarUtil
import com.vanzoo.novelsdk.NovelSdk
import com.vanzoo.novelsdk.ui.activity.NovelFragment

class MainActivity : AppCompatActivity() {
    private var mViewPager: ViewPager? = null
    private var mBottomNavigationView: BottomNavigationView? = null


    private val mFragments = mutableListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //start 全屏界面实现沉浸式状态栏效果，需要将root布局整体下移状态栏的高度。若不想要此效果，可以忽略这段代码
        StatusBarUtil.setStatusBarTranslucent(this)
        val statusBarHeight: Int = ScreenUtils.getStatusBarHeight(this)
        val rootView: View =
            (window.decorView.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        rootView.setPadding(0, statusBarHeight, 0, 0)
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorbg))
        //end 全屏界面实现沉浸式状态栏效果，需要将root布局整体下移状态栏的高度。若不想要此效果，可以忽略这段代码

        initView()
    }

    private fun initView() {
        mViewPager = findViewById(R.id.viewPager)
        mBottomNavigationView = findViewById(R.id.bottom_navigation)

        mViewPager?.offscreenPageLimit = 2

        val novelFragment = NovelSdk.getNovelFragment()
        val homeFragment = HomeFragment()
        if (novelFragment != null) {
            mFragments.add(novelFragment)
        }
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