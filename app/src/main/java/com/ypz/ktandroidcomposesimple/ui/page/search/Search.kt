package com.ypz.ktandroidcomposesimple.ui.page.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.Hotkey
import com.ypz.ktandroidcomposesimple.tools.RouteTools
import com.ypz.ktandroidcomposesimple.tools.WanAndroidUserTools
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.theme.BaseFullStatusTitleModifier
import com.ypz.ktandroidcomposesimple.ui.theme.ToolBarHeight
import com.ypz.ktandroidcomposesimple.ui.weidget.*
import com.ypz.ktandroidcomposesimple.view.module.login.LoginViewAction
import com.ypz.ktandroidcomposesimple.view.module.search.SearchViewAction
import com.ypz.ktandroidcomposesimple.view.module.search.SearchViewModule

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: SearchViewModule = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    val keyboardCtrl = LocalSoftwareKeyboardController.current
    val hotkeys = viewStates.hotKeys
    val queries = viewStates.searchResult?.collectAsLazyPagingItems()
    Column(Modifier.fillMaxSize().navigationBarsPadding() .pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                keyboardCtrl?.hide()
            }
        )
    }) {

        SearchHead(
            keyWord = viewStates.searchContent,
            onTextChange = {
                viewModel.dispatch(SearchViewAction.SetSearchKey(it))
            },
            onSearchClick = {
                if (it.trim().isNotEmpty()) {
                    viewModel.dispatch(SearchViewAction.Search)
                }
                keyboardCtrl?.hide()
            },
            navController = navCtrl
        )

        LazyColumn {

            if (hotkeys.isNotEmpty()) {
                // part1. 搜索热词
                stickyHeader {
                    ListTitle(title = "搜索热词")
                }
                item {
                    Box {
                        HotkeyItem(
                            hotkeys = hotkeys,
                            onSelected = { text ->
                                viewModel.dispatch(SearchViewAction.SetSearchKey(text))
                                viewModel.dispatch(SearchViewAction.Search)
                            })
                    }
                }

            }


            if (queries != null) {
                // part2. 搜索列表
                stickyHeader {
                    ListTitle(title = "搜索结果")
                }

                itemsIndexed(queries) { index, item ->
                    MultiStateItemView(
                        data = item!!,
                        onSelected = { data ->
                            RouteTools.navTo(
                                navCtrl,
                                AppRouter.WEB_VIEW,
                                data
                            )
                        })
                }
            }
        }

        BackHandler(enabled = true) {

            keyboardCtrl?.hide()
            navCtrl.popBackStack()

        }
    }
}

/**
 * 搜索框
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchHead(
    keyWord: String,
    onTextChange: (text: String) -> Unit,
    onSearchClick: (key: String) -> Unit,
    navController: NavHostController
) {
    val keyboardCtrl = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.BaseFullStatusTitleModifier()) {

        Column(Modifier.fillMaxWidth()) {

            Spacer(modifier = Modifier.padding(top = 24.dp))

            AppToolsBar(title = "Search", onBack = {
                keyboardCtrl?.hide()
                navController.popBackStack()
            })

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 5.dp, bottom = 5.dp)
            ) {
                Spacer(modifier = Modifier.size(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .weight(1f)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(14.dp),
                        )

                ) {
                    BasicTextField(
                        value = keyWord,
                        onValueChange = { onTextChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(start = 12.dp, top = 5.dp, end = 12.dp, bottom = 5.dp),
                        maxLines = 1,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearchClick(keyWord) }),
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))
            }

            Spacer(modifier = Modifier.padding(top = 13.8.dp))
        }


    }
}

/**
 * 搜索热词的item
 */
@Composable
fun HotkeyItem(
    hotkeys: List<Hotkey>,
    onSelected: (key: String) -> Unit
) {
    FlowRow(Modifier.padding(10.dp)) {
        hotkeys.forEach {
            LabelTextButton2(
                text = it.name ?: "",
                isSelect = false,
                modifier = Modifier.padding(end = 5.dp, bottom = 5.dp).background(Color.Gray.copy(0.88f),RoundedCornerShape(16.dp)),
                onClick = {
                    onSelected(it.name!!)
                }
            )
        }
    }
}