package com.binh.core.example.ui.home.hometab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.binh.core.example.databinding.FragmentHomeTabBinding
import com.binh.core.example.ui.base.HostFragment
import com.binh.core.example.ui.home.HomeFragmentDirections
import com.binh.core.ui.ext.parentNavController
import com.binh.core.ui.ext.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeTabFragment : HostFragment<FragmentHomeTabBinding>() {

    override val viewModel: HomeTabViewModel by viewModels()

    override fun inflateLayout(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ): FragmentHomeTabBinding {
        return FragmentHomeTabBinding.inflate(layoutInflater, viewGroup, attachToParent)
    }

    override fun onStateChanged() {
        binding.login.setSafeOnClickListener {
            parentNavController?.navigate(HomeFragmentDirections.actionHomeGraphToLoginGraph())
        }
    }

}