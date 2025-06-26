package com.binh.core.example.ui.base

import androidx.viewbinding.ViewBinding
import com.binh.core.example.components.dialog.LoadingDialog
import com.binh.core.ui.BaseBindingFragment

abstract class HostFragment<VB : ViewBinding> : BaseBindingFragment<VB>() {


    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }


    override fun onSharedStateChanged() {
        viewModel.failureEvent.consume {
            it?.getContentIfNotHandled()?.let { failure ->
                // handle show error
            }
        }
        viewModel.loadingEvent.consume {
            it?.getContentIfNotHandled()?.let {
                if (it)
                    showLoading()
                else
                    hideLoading()
            }
        }
    }


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