package com.binh.core.ui.ext

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

val Fragment.parentNavController get() = parentFragment?.parentFragment?.findNavController()

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

inline fun <T> Fragment.observeNavigationResultLiveData(
    key: String = "result",
    crossinline action: (T) -> Unit
) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner) {
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
            action(it)
        }

