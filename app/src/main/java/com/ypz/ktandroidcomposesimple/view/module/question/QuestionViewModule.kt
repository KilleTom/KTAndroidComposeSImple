package com.ypz.ktandroidcomposesimple.view.module.question

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.ypz.ktandroidcomposesimple.common.paging.easyPager
import com.ypz.ktandroidcomposesimple.net.wanAndroid.api.WanAndroidNetService
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.Article
import com.ypz.ktandroidcomposesimple.ui.data.PagingArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class QuestionViewModule @Inject constructor(
    private var service: WanAndroidNetService,
) : ViewModel() {
    private val pager by lazy {
        easyPager {
            service.getWendaData(it)
        }
    }
    var viewStates by mutableStateOf(QuestionViewState(pagingData = pager))
        private set
}

data class QuestionViewState(
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
    val pagingData: PagingArticle
)
