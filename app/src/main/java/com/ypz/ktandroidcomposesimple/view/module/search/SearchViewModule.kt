package com.ypz.ktandroidcomposesimple.view.module.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ypz.ktandroidcomposesimple.common.paging.easyPager
import com.ypz.ktandroidcomposesimple.net.wanAndroid.api.WanAndroidNetService
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.Hotkey
import com.ypz.ktandroidcomposesimple.ui.data.PagingArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModule @Inject constructor(
    private var service: WanAndroidNetService
) : ViewModel() {
    var viewStates by mutableStateOf(SearchViewState())
        private set

    init {
        dispatch(SearchViewAction.GetHotKeys)
    }

    fun dispatch(action: SearchViewAction) {
        when (action) {
            is SearchViewAction.Search -> search()
            is SearchViewAction.GetHotKeys -> getHotKeys()
            is SearchViewAction.SetSearchKey -> setSearchKey(action.key)
        }
    }

    private fun setSearchKey(key: String) {
        viewStates = viewStates.copy(searchContent = key)
    }

    private fun getHotKeys() {
        viewModelScope.launch {
            flow {
                emit(service.getHotkeys())
            }.map {
                it.data ?: emptyList()
            }.onEach {
                viewStates = viewStates.copy(hotKeys = it)
            }.catch {

            }.collect()
        }
    }

    private fun search() {
        val key = viewStates.searchContent
        val paging = easyPager {
            service.queryArticle(page = it, key = key)
        }.cachedIn(viewModelScope)
        viewStates = viewStates.copy(searchResult = paging)
    }
}


data class SearchViewState(
    val searchResult: PagingArticle? = null,
    val searchContent: String = "",
    val hotKeys: List<Hotkey> = emptyList()
)

sealed class SearchViewAction {
    object GetHotKeys : SearchViewAction()
    object Search : SearchViewAction()
    data class SetSearchKey(val key: String) : SearchViewAction()
}
