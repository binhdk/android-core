package com.binh.core.ui.component.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class PagingScrollListener(
    private val layoutManager: LayoutManager,
    private var visibleThreshold: Int = 3,
) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition =
            when (layoutManager) {
                is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions()
                is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                else -> return
            }
        if (!isLoading && !isLastPage) {
            if (firstVisibleItemPosition + visibleThreshold >= totalItemCount - visibleItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean
}

fun StaggeredGridLayoutManager.findFirstVisibleItemPositions(): Int {
    val array = IntArray(this.spanCount)
    return this.findFirstVisibleItemPositions(array).minOrNull() ?: -1
}

fun StaggeredGridLayoutManager.findLastVisibleItemPosition(): Int {
    val array = IntArray(this.spanCount)
    return this.findLastVisibleItemPositions(array).maxOrNull() ?: -1
}