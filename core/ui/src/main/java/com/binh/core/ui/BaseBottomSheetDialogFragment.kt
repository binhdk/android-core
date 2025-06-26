package com.binh.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.binh.core.ui.util.Event
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseBottomSheetDialogFragment :
    BottomSheetDialogFragment() {

    abstract val layoutId: Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monitorNetwork()
        onSharedStateChanged()
        onStateChanged()
        initView()
        observeViewEvents()
        onPostViewCreated(view, savedInstanceState)
    }

    open fun onPostViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    fun setPeekHeight(height: Int) {
        (dialog as? BottomSheetDialog)?.behavior?.peekHeight = height
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        changeSheetContainerSize(sheetContainer)
    }

    open fun changeSheetContainerSize(sheetContainer: ViewGroup) {
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }


    open fun initViewModel() {
        //
    }

    open fun initData() {
        //init fetch data
    }

    open fun initView() {
        //
    }

    open fun observeViewEvents() {
        //
    }

    open fun onStateChanged() {
        // update ui
    }

    open fun onSharedStateChanged() {
        // update ui
    }

    open fun monitorNetwork() {
        //
    }

    open fun onNetworkStatusChanged(isOnline: Boolean) {
        //
    }

    inline fun <T> Flow<T>.consume(crossinline action: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect { action(it) }
            }
        }
    }

    inline fun <T> Flow<Event<T>>.consumeEvent(crossinline action: (T) -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect { it.getContentIfNotHandled()?.let { action(it) } }
            }
        }
    }
}