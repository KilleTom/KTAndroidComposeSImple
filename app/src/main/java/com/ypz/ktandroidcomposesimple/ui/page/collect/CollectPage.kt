package com.ypz.ktandroidcomposesimple.ui.page.collect

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.flowlayout.FlowRow
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.WebData
import com.ypz.ktandroidcomposesimple.tools.RouteTools
import com.ypz.ktandroidcomposesimple.tools.WanAndroidUserTools
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.theme.BaseFullStatusTitleModifier
import com.ypz.ktandroidcomposesimple.ui.weidget.*
import com.ypz.ktandroidcomposesimple.view.module.collect.CollectViewAction
import com.ypz.ktandroidcomposesimple.view.module.collect.CollectViewModule

@ExperimentalFoundationApi
@Composable
fun CollectPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: CollectViewModule = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    val collectPaging = viewStates.pagingData?.collectAsLazyPagingItems()
    val webUrls = viewStates.urlList
    val titles = viewStates.titles
    val isRefreshing = viewStates.isRefreshing
    val listState =
        if ((collectPaging?.itemCount ?: 0) > 0) viewStates.listState else LazyListState()

    DisposableEffect(Unit) {
        Log.i("debug", "onStart")
        viewModel.dispatch(CollectViewAction.OnStart)
        onDispose {
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(modifier = Modifier.BaseFullStatusTitleModifier()){
            Column(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.padding(top = 24.dp))
                AppToolsBar(title = " ${WanAndroidUserTools.userInfo?.nickname?:""} Collection")
            }
        }



        if (!viewStates.isLogged) {
//            EmptyView(
//                tips = "点击登录",
//                imageVector = Icons.Default.Face
//            ) {
//                RouteUtils.navTo(navCtrl, RouteName.LOGIN)
//            }
        } else {
            collectPaging?.let { it ->
                RefreshWidget(it, listState = listState, isRefreshing = isRefreshing, onRefresh = {
                    viewModel.dispatch(CollectViewAction.Refresh)
                }) {
//                    if (!webUrls.isNullOrEmpty()) {
//                        stickyHeader {
//                            ListTitle(title = titles[1].text)
//                        }
//                        item {
//                            FlowRow(
//                                modifier = Modifier.padding(10.dp)
//                            ) {
//                                webUrls.forEachIndexed { index, website ->
//                                    LabelTextButton(
//                                        text = website.name ?: "标签",
//                                        modifier = Modifier.padding(end = 10.dp, bottom = 10.dp),
//                                        onClick = {
//                                            website.link?.let {link->
//                                                RouteTools.navTo(
//                                                    navCtrl,
//                                                    AppRouter.WEB_VIEW,
//                                                    WebData(website.name, link))
//                                            }
//                                        }
//                                    )
//                                }
//                            }
//                        }
//                    }

                    if (collectPaging.itemCount > 0) {

                        stickyHeader {
                            ListTitle(title = titles[0].text)
                        }

                        items(collectPaging) { collectItem ->
                            CollectItemView(
                                collectItem!!,
                                onClick = {
                                    RouteTools.navTo(
                                        navCtrl,
                                        AppRouter.WEB_VIEW,
                                        WebData(collectItem.title, collectItem.link)
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}