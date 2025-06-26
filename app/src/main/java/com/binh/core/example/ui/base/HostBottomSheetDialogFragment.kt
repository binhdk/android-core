package com.binh.core.example.ui.base

import androidx.viewbinding.ViewBinding
import com.binh.core.example.components.dialog.LoadingDialog
import com.binh.core.ui.BaseBindingBottomSheetDialogFragment

abstract class HostBottomSheetDialogFragment<VB : ViewBinding> :
    BaseBindingBottomSheetDialogFragment<VB>() {

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog.dismiss()
    }

    fun showLoading() {
        if (!loadingDialog.isShowing) loadingDialog.show()
    }

    fun hideLoading() {
        loadingDialog.dismiss()
    }
}