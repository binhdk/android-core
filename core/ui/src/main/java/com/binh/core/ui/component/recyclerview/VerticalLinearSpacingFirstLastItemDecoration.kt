package com.binh.core.ui.component.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class VerticalLinearSpacingFirstLastItemDecoration(private val spacing: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)
        val isLast = position == state.itemCount - 1
        if (position == 0) {
            outRect.top = spacing
        } else if (isLast) {
            outRect.bottom = spacing
        }

    }
}
