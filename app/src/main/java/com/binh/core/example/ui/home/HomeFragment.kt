package com.binh.core.example.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.binh.core.example.R
import com.binh.core.example.databinding.FragmentHomeBinding
import com.binh.core.example.ui.base.HostFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : HostFragment<FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun inflateLayout(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, viewGroup, attachToParent)
    }

    override fun initView() {
        super.initView()
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
    }

}