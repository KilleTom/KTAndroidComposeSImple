package com.ypz.ktandroidcomposesimple.ui.page.home.recommend

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.WebData
import com.ypz.ktandroidcomposesimple.tools.RouteTools
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.weidget.Banner
import com.ypz.ktandroidcomposesimple.ui.weidget.MultiStateItemView
import com.ypz.ktandroidcomposesimple.ui.weidget.RefreshWidget
import com.ypz.ktandroidcomposesimple.view.module.recommend.RecommendViewAction
import com.ypz.ktandroidcomposesimple.view.module.recommend.WanAndroidRecommendViewModule

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WanAndroidRecommendPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: WanAndroidRecommendViewModule = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    val recommendData = viewStates.pagingData.collectAsLazyPagingItems()
    val banners = viewStates.imageList
    val topArticle = viewStates.topArticles
    val isRefreshing = viewStates.isRefreshing
    val listState = if (recommendData.itemCount > 0) viewStates.listState else LazyListState()

    RefreshWidget(
        recommendData, listState = listState, isRefreshing = isRefreshing,
        onRefresh = { viewModel.dispatch(RecommendViewAction.Refresh) },
    ) {
        if (banners.isNotEmpty()) {
            item {
                Banner(list = banners) { url, title ->
                    RouteTools.navTo(navCtrl, AppRouter.WEB_VIEW, WebData(title, url))
                }
            }
        }
        if (topArticle.isNotEmpty()) {
            itemsIndexed(topArticle) { index, item ->
                MultiStateItemView(
                    modifier = Modifier.padding(top = if (index == 0) 5.dp else 0.dp),
                    data = item,
                    isTop = true,
                    onSelected = { data ->
                        RouteTools.navTo(navCtrl, AppRouter.WEB_VIEW, data)
                    }
                )
            }
        }

        itemsIndexed(recommendData) { _, item ->
            MultiStateItemView(
                data = item!!,
                onSelected = {
                    RouteTools.navTo(navCtrl, AppRouter.WEB_VIEW, it)
                })
        }
    }
}