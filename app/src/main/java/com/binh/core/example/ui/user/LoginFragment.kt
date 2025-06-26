package com.binh.core.example.ui.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.binh.core.example.databinding.FragmentLoginBinding
import com.binh.core.example.ui.base.HostFragment
import com.binh.core.ui.ext.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : HostFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun inflateLayout(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater, viewGroup, attachToParent)
    }


    override fun observeViewEvents() {
        binding.login.setSafeOnClickListener {
            viewModel.login("admin@gmail.com", "admin1234!")
//            findNavController().navigate(LoginFragmentDirections.actionLoginToForgotPassword())
        }
    }

    override fun onStateChanged() {
        viewModel.loginEvent.consume {
            Log.d(TAG, it.toString())
        }
    }

    companion object {
        const val TAG = "LoginFragment"
    }

}