package com.ypz.ktandroidcomposesimple.common.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ypz.ktandroidcomposesimple.LearnAndroidApp
import com.ypz.ktandroidcomposesimple.common.net.HttpResult
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.BasicBean
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.ListWrapper
import com.ypz.ktandroidcomposesimple.tools.NetTools
import com.ypz.ktandroidcomposesimple.tools.showToast
import kotlinx.coroutines.flow.Flow

fun <T : Any> ViewModel.easyPager(
    config: BasePagingConfig = BasePagingConfig(),
    callAction: suspend (page: Int) -> BasicBean<ListWrapper<T>>
): Flow<PagingData<T>> {
    return basePager(config, 0) {
        val page = it.key ?: 0
        val response = try {
            HttpResult.Success(callAction.invoke(page))
        } catch (e: Exception) {
            if (NetTools.checkNetUsefull(LearnAndroidApp.CONTEXT).not()) {
                showToast("没有网络,请重试")
            } else {
                showToast("请求失败，请重试")
            }
            HttpResult.Error(e)
        }
        when (response) {
            is HttpResult.Success -> {

                val data = response.result.data

                val hasNotNext = (data!=null)&&(data.size< it.loadSize) && (data.over)

                PagingSource.LoadResult.Page(
                    data = data?.datas?: emptyList(),
                    prevKey = if (page - 1 > 0) page - 1 else null,
                    nextKey = if (hasNotNext) null else page + 1
                )
            }
            is HttpResult.Error -> {
                PagingSource.LoadResult.Error(response.exception)
            }
        }
    }
}

fun <K : Any, V : Any> ViewModel.basePager(
    config: BasePagingConfig = BasePagingConfig(),
    initialKey: K? = null,
    loadData: suspend (PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>
): Flow<PagingData<V>> {
    val baseConfig = PagingConfig(
        config.pageSize,
        initialLoadSize = config.initialLoadSize,
        prefetchDistance = config.prefetchDistance,
        maxSize = config.maxSize,
        enablePlaceholders = config.enablePlaceholders
    )
    return Pager(
        config = baseConfig,
        initialKey = initialKey
    ) {
        object : PagingSource<K, V>() {
            override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
                return loadData.invoke(params)
            }

            override fun getRefreshKey(state: PagingState<K, V>): K? {
                return initialKey
            }

        }
    }.flow.cachedIn(viewModelScope)
}