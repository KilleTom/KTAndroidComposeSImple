package com.ypz.ktandroidcomposesimple.view.module.collect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ypz.ktandroidcomposesimple.common.paging.easyPager
import com.ypz.ktandroidcomposesimple.net.wanAndroid.api.WanAndroidNetService
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.CollectBean
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.ParentBean
import com.ypz.ktandroidcomposesimple.tools.WanAndroidUserTools
import com.ypz.ktandroidcomposesimple.ui.weidget.TabTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectViewModule @Inject constructor(
    private var service: WanAndroidNetService
) : ViewModel() {

    private val pager by lazy {
        easyPager { service.getCollectionList(it) }.cachedIn(viewModelScope)
    }
    var viewStates by mutableStateOf(CollectViewState())
        private set

    fun dispatch(action: CollectViewAction) {
        when (action) {
            is CollectViewAction.OnStart -> onStart()
            is CollectViewAction.Refresh -> refresh()
        }
    }

    private fun onStart() {
        viewStates = viewStates.copy(isLogged = WanAndroidUserTools.isLogged)
        if (viewStates.isLogged && viewStates.isInit.not()) {
            viewStates = viewStates.copy(isInit = true)
            initData()
        }
    }

    private fun initData() {
        viewStates = viewStates.copy(pagingData = pager)
        fetchUrls()
    }

    private fun refresh() {
        fetchUrls()
    }

    private fun fetchUrls() {
        viewModelScope.launch {
            flow {
                emit(service.getCollectUrls())
            }.onStart {
                viewStates = viewStates.copy(isRefreshing = true)
            }.map {
                it.data ?: emptyList()
            }.onEach {
                viewStates = viewStates.copy(urlList = it, isRefreshing = false)
            }.catch {
                viewStates = viewStates.copy(isRefreshing = false)
            }.collect()
        }
    }

}

data class CollectViewState(
    val isInit: Boolean = false,
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
    val urlList: List<ParentBean> = emptyList(),
    val pagingData: PagingCollect? = null,
    val isLogged: Boolean = WanAndroidUserTools.isLogged,
    val titles: List<TabTitle> = listOf(
        TabTitle(301, "文章列表"),
        TabTitle(302, "我的网址"),
    )
)

sealed class CollectViewAction {
    object OnStart : CollectViewAction()
    object Refresh : CollectViewAction()
}

typealias PagingCollect = Flow<PagingData<CollectBean>>