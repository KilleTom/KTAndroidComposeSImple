package com.ypz.ktandroidcomposesimple.view.module.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ypz.ktandroidcomposesimple.ui.weidget.TabTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModule@Inject constructor() : ViewModel() {

    var viewStates by mutableStateOf(HomeViewState())
        private set

    val pageIndex by mutableStateOf(0)

    init {
        viewStates = viewStates.copy(
            titles = listOf(
                TabTitle(0x101, "广场"),
                TabTitle(0x102, "推荐"),
                TabTitle(0x103, "问答")
            )
        )
    }
}

data class HomeViewState(val titles: List<TabTitle> = emptyList())