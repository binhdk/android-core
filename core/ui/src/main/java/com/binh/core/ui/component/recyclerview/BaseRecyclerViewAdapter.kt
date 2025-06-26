package com.binh.core.ui.component.recyclerview

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binh.core.ui.ext.setSafeOnClickListener

abstract class BaseRecyclerViewAdapter<T, VH : BaseViewHolder<T>>(diffCallback: BaseDiffCallback<T>) :
    ListAdapter<T, VH>(diffCallback) {

    var onItemClick: ((item: T, position: Int) -> Unit)? = null

    protected open fun onItemClick(position: Int) {
        currentList.getOrNull(position)?.let {
            onItemClick?.invoke(it, position)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        currentList.getOrNull(position)?.let {
            holder.bind(it)
        }
    }
}

abstract class BaseDiffCallback<T> : DiffUtil.ItemCallback<T>()

abstract class BaseViewHolder<T>(
    private val itemView: View,
    private val onItemClick: ((position: Int) -> Unit)? = null,
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setSafeOnClickListener {
            onItemClick?.invoke(adapterPosition)
        }
    }

    open fun bind(item: T) = Unit
}

abstract class BaseSingleSelectionRecyclerViewAdapter<T, VH : BaseSingleSelectionViewHolder<T>>(
    diffCallback: BaseDiffCallback<T>
) : BaseRecyclerViewAdapter<T, VH>(diffCallback) {

    var selectedPosition = -1
        private set(value) {
            val oldPosition = field
            field = value
            notifyItemChanged(oldPosition)
            notifyItemChanged(field)
        }

    val selectedItem: T? get() = currentList.getOrNull(selectedPosition)


    override fun onBindViewHolder(holder: VH, position: Int) {
        currentList.getOrNull(position)?.let {
            holder.bind(it, selectedPosition)
        }
    }

    override fun onItemClick(position: Int) {
        currentList.getOrNull(position)?.let {
            selectedPosition = position
            onItemClick?.invoke(it, position)
        }
    }

    fun submitListAndGetSelectedItem(list: MutableList<T>?): Pair<T?, Int> {
        submitList(list)
        return Pair(selectedItem, selectedPosition)
    }

    override fun submitList(list: MutableList<T>?) {
        val prevSelectedItem = selectedItem
        super.submitList(list)

        // update selected position
        prevSelectedItem?.let { updateSelectedPosition(it) } ?: run { selectedPosition = -1 }
    }

    abstract fun updateSelectedPosition(item: T)
}

abstract class BaseSingleSelectionViewHolder<T>(
    private val itemView: View,
    onItemClick: ((position: Int) -> Unit),
) : BaseViewHolder<T>(itemView, onItemClick) {
    open fun bind(item: T, selectedPosition: Int) = Unit
}
