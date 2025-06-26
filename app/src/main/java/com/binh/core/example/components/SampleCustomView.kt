package com.binh.core.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.binh.core.example.databinding.LayoutSampleCustomViewBinding

class SampleCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding =
        LayoutSampleCustomViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {

    }
}