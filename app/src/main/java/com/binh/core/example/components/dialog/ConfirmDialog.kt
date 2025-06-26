package com.binh.core.example.components.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.FragmentActivity
import com.binh.core.example.databinding.DialogConfirmBinding
import com.binh.core.ui.ext.setSafeOnClickListener

class ConfirmDialog(
    activity: FragmentActivity,
    private val title: String,
    private val message: String,
    private val onConfirmClick: () -> Unit,
    private val cancelable: Boolean = false,
    private val cancelTouchOutside: Boolean = false
) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(cancelTouchOutside)
        setCancelable(cancelable)
        val binding = DialogConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.run {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        binding.btnClose.setSafeOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setSafeOnClickListener {
            dismiss()
            onConfirmClick.invoke()
        }

        binding.tvTitle.text = title
        binding.tvContent.text = message

    }
}