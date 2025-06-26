package com.binh.core.ui

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding


abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity() {

    protected lateinit var binding: VB

    abstract fun inflateLayout(layoutInflater: LayoutInflater): VB

    override fun onCreateContentView() {
        binding = inflateLayout(layoutInflater)
        setContentView(binding.root)
    }
}