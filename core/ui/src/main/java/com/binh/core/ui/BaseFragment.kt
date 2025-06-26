package com.binh.core.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.binh.core.common.NetworkMonitor
import com.binh.core.ui.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    abstract val layoutId: Int

    // override in subclass
    // override val viewModel: ***ViewModel by viewModels()
    abstract val viewModel: BaseViewModel

    @Inject
    lateinit var networkMonitor: NetworkMonitor


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
        onStateChanged()
        onPostViewCreated(view, savedInstanceState)
    }

    open fun onPostViewCreated(view: View, savedInstanceState: Bundle?) {

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
        networkMonitor.isOnline.consume {
            Log.d(javaClass.simpleName, "isOnline - $it")
            onNetworkStatusChanged(it)
        }
    }

    open fun onNetworkStatusChanged(isOnline: Boolean) {
        //
    }

    inline fun <T> Flow<T>.consume(crossinline action: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect { action(it) }
            }
        }
    }

    inline fun <T> Flow<Event<T>>.consumeEvent(crossinline action: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect { it.getContentIfNotHandled()?.let { action(it) } }
            }
        }
    }
}