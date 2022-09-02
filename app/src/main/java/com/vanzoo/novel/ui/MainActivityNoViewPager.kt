package com.vanzoo.novel.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.vanzoo.novel.R
import com.vanzoo.novelsdk.NovelSdk
import com.vanzoo.novelsdk.ui.activity.NovelFragment
import kotlinx.android.synthetic.main.activity_main_no_pageview.view.*

class MainActivityNoViewPager : AppCompatActivity() {
    private lateinit var mFrame: FrameLayout
    private lateinit var mTab: TabLayout

    private var mNovelFragment: NovelFragment? = null
    private var mHomeFragment: HomeFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_no_pageview)
        initView()
        initFragments()
        initTab()
    }

    private fun initView() {
        mFrame = findViewById(R.id.frame_layout)
        mTab = findViewById(R.id.tab)

    }

    private fun initTab(){
        mTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, mNovelFragment!!)
                                .commitAllowingStateLoss()
                            mNovelFragment?.userVisibleHint = true
                        }
                        1 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, mHomeFragment!!)
                                .commitAllowingStateLoss()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        mTab.addTab(mTab.newTab().setText("小说"),true)
        mTab.addTab(mTab.newTab().setText("首页"))
    }

    private fun initFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        mNovelFragment = NovelSdk.getNovelFragment()
        mHomeFragment = HomeFragment()
        if (mNovelFragment != null) {
            transaction.add(R.id.frame_layout, mNovelFragment!!, "novel")
        }
        if (mHomeFragment != null) {
            transaction.add(R.id.frame_layout, mHomeFragment!!, "home")
        }
        transaction.commit()
    }

}