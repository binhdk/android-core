package com.binh.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BaseBindingBottomSheetDialogFragment<VB : ViewBinding> :
    BaseBottomSheetDialogFragment() {

    override val layoutId: Int
        get() = View.NO_ID


    private var _binding: VB? = null

    val binding
        get() = _binding!!

    val isViewCreated: Boolean
        get() = _binding != null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflateLayout(inflater, container, false).apply {
            _binding = this
        }.root

    }

    abstract fun inflateLayout(
        layoutInflater: LayoutInflater, viewGroup: ViewGroup?, attachToParent: Boolean = false
    ): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}