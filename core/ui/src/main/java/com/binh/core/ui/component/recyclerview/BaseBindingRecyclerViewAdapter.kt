package com.binh.core.ui.component.recyclerview

import androidx.viewbinding.ViewBinding

abstract class BaseBindingRecyclerViewAdapter<T, VH : BaseBindingViewHolder<T>>(diffCallback: BaseDiffCallback<T>) :
    BaseRecyclerViewAdapter<T, VH>(diffCallback)

abstract class BaseBindingViewHolder<T>(
    private val binding: ViewBinding,
    onItemClick: ((position: Int) -> Unit)? = null,
) : BaseViewHolder<T>(binding.root, onItemClick)


abstract class BaseBindingSingleSelectionRecyclerViewAdapter<T, VH : BaseBindingSingleSelectionViewHolder<T>>(
    diffCallback: BaseDiffCallback<T>
) : BaseSingleSelectionRecyclerViewAdapter<T, VH>(diffCallback)

abstract class BaseBindingSingleSelectionViewHolder<T>(
    private val binding: ViewBinding,
    onItemClick: ((position: Int) -> Unit),
) : BaseSingleSelectionViewHolder<T>(binding.root, onItemClick)
