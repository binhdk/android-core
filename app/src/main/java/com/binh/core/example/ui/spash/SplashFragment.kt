package com.binh.core.example.ui.spash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.binh.core.example.databinding.FragmentSplashBinding
import com.binh.core.example.ui.base.HostFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : HostFragment<FragmentSplashBinding>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun inflateLayout(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(layoutInflater, viewGroup, attachToParent)
    }

    override fun onPostViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onPostViewCreated(view, savedInstanceState)
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeGraph())
    }
}