package com.ypz.ktandroidcomposesimple.view.module.square

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
import com.ypz.ktandroidcomposesimple.ui.data.PagingArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WanAndroidSquareViewModule @Inject constructor(
    private var service: WanAndroidNetService,
) : ViewModel() {
    private val pager by lazy {
        easyPager {
            delay(2000)
            service.getSquareData(it)
        }.cachedIn(viewModelScope)
    }
    var viewStates by mutableStateOf(SquareViewState(pagingData = pager))
        private set
}

data class SquareViewState(
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
    val pagingData: PagingArticle
)

sealed class SquareViewAction {
    object Refresh : SquareViewAction()
}

