package com.binh.core.example.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.binh.core.example.databinding.LayoutRecyclerItemBinding
import com.binh.core.ui.component.recyclerview.BaseBindingRecyclerViewAdapter
import com.binh.core.ui.component.recyclerview.BaseBindingViewHolder
import com.binh.core.ui.component.recyclerview.BaseDiffCallback

class SampleRecyclerAdapter :
    BaseBindingRecyclerViewAdapter<String, SampleRecyclerAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        val diffCallback = object : BaseDiffCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(binding: ViewBinding) :
        BaseBindingViewHolder<String>(binding, ::onItemClick)

}