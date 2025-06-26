package com.binh.core.ui.util

import com.binh.core.data.util.DEFAULT_LIMIT
import kotlin.math.max

class PagingMetaData(
    currentPage: Int = 0,
    limit: Int = DEFAULT_LIMIT,
    totalPage: Int = 0,
    var lastId: Long? = null,
) {
    @Volatile
    var currentPage: Int = currentPage
        set(value) {
            field = max(0, value)
        }

    @Volatile
    var limit: Int = limit
        set(value) {
            field = max(1, value)
        }

    @Volatile
    var totalPage: Int = totalPage
        set(value) {
            field = max(0, value)
        }

    @get:Synchronized
    val isLastPage: Boolean get() = currentPage >= totalPage

    @Volatile
    var isLoading: Boolean = false
}