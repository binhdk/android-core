package com.binh.core.example.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.binh.core.example.databinding.FragmentForgotPasswordBinding
import com.binh.core.example.ui.base.HostFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordFragment :
    HostFragment<FragmentForgotPasswordBinding>() {
    override fun inflateLayout(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ): FragmentForgotPasswordBinding {
        return FragmentForgotPasswordBinding.inflate(layoutInflater, viewGroup, attachToParent)
    }

    override val viewModel: ForgotPasswordViewModel by viewModels()
}