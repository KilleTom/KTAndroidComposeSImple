package com.ypz.ktandroidcomposesimple.ui.page.home.square

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ypz.ktandroidcomposesimple.tools.RouteTools
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.weidget.MultiStateItemView
import com.ypz.ktandroidcomposesimple.ui.weidget.RefreshWidget
import com.ypz.ktandroidcomposesimple.view.module.square.WanAndroidSquareViewModule

@Composable
fun WanAndroidSquare(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: WanAndroidSquareViewModule = hiltViewModel()
) {
    val viewStates = remember { viewModel.viewStates }
    val squareData = viewStates.pagingData.collectAsLazyPagingItems()
    val listState = if (squareData.itemCount > 0) viewStates.listState else LazyListState()

    val uiSystem = rememberSystemUiController()

    RefreshWidget(squareData, listState = listState) {
        itemsIndexed(squareData) { _, item ->
            MultiStateItemView(
                data = item!!,
                onSelected = {
                    RouteTools.navTo(navCtrl, AppRouter.WEB_VIEW, it)
                })
        }
    }
}