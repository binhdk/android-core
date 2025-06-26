package com.binh.core.ui.ext

import android.content.res.Resources
import android.graphics.Rect
import android.util.Size
import android.util.TypedValue
import kotlin.math.roundToInt

fun Rect.toSize(): Size {
    return Size(right - left, bottom - top)
}


val Number.dpToPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

val Number.dpFToPxF
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

val Number.pxToDp
    get() = (toFloat() / Resources.getSystem().displayMetrics.density).roundToInt()

val Number.pxFToDpF
    get() = (toFloat() / Resources.getSystem().displayMetrics.density)


fun Resources.pxToDp(px: Int): Int {
    return (px / displayMetrics.density).toInt()
}

fun Resources.pxToDpF(px: Float): Float {
    return (px / displayMetrics.density)
}


