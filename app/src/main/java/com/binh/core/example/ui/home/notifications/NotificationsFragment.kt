package com.binh.core.example.ui.home.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.binh.core.example.databinding.FragmentNotificationsBinding
import com.binh.core.example.ui.base.HostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : HostFragment<FragmentNotificationsBinding>() {

    override fun inflateLayout(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ): FragmentNotificationsBinding {
        return FragmentNotificationsBinding.inflate(layoutInflater, viewGroup, attachToParent)
    }

    override val viewModel: NotificationsViewModel by viewModels()


}