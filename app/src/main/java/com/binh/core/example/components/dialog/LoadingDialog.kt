package com.binh.core.example.components.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.binh.core.example.databinding.DialogLoadingBinding

class LoadingDialog(
    activity: Activity
) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.run {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.0f)
        }
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        val binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}