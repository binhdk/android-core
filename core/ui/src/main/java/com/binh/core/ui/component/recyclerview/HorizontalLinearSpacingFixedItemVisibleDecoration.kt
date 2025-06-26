package com.binh.core.ui.component.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class HorizontalLinearSpacingFixedItemVisibleDecoration(
    private val fixedItem: Int,
    private val itemWidth: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val column = position % fixedItem
        val parentWidth = parent.width - parent.paddingLeft - parent.paddingRight
        val spacing = (parentWidth - itemWidth * fixedItem) / (fixedItem + 1)
        outRect.left = spacing - column * spacing / fixedItem
        outRect.right = (column + 1) * spacing / fixedItem
    }
}
