package com.vanzoo.novel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vanzoo.novel.R

class HomeFragment:Fragment() {

    private var rootView:View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        initView()
        return rootView
    }

    private fun initView(){

    }
}