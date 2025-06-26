package com.binh.core.ui.util

import kotlinx.coroutines.flow.MutableStateFlow

data class PagingData<T>(
    val data: MutableList<T> = mutableListOf(),
    val pagingMetaData: PagingMetaData = PagingMetaData(),
    val dataState: MutableStateFlow<MutableList<T>> = MutableStateFlow(mutableListOf()),
) {

    fun onDataLoaded(loaded: List<T>, page: Int) {
        if (page <= 1) {
            data.clear()
        }

        data.addAll(loaded)
        dataState.value = data
    }

    fun onDataLoaded(loaded: List<T>, lastId: Long? = null) {
        if (lastId == null) {
            data.clear()
        }

        data.addAll(loaded)
        dataState.value = data
    }

    fun updatePagingMetaData(currentPage: Int?, lastId: Long?, totalPage: Int?, limit: Int?) {
        currentPage?.let { pagingMetaData.currentPage = it }
        lastId?.let { pagingMetaData.lastId = it }
        totalPage?.let { pagingMetaData.totalPage = it }
        limit?.let { pagingMetaData.limit = it }
    }
}
