package com.binh.core.example.ui.home.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.binh.core.example.ui.base.HostFragment
import com.binh.core.example.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardFragment : HostFragment<FragmentDashboardBinding>() {

    override fun inflateLayout(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(layoutInflater, viewGroup, attachToParent)
    }

    override val viewModel: DashboardViewModel by viewModels()
}