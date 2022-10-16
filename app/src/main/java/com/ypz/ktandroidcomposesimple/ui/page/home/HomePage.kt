package com.ypz.ktandroidcomposesimple.ui.page.home

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ypz.ktandroidcomposesimple.ui.page.home.question.QuestionPage
import com.ypz.ktandroidcomposesimple.ui.page.home.recommend.WanAndroidRecommendPage
import com.ypz.ktandroidcomposesimple.ui.page.home.square.WanAndroidSquare
import com.ypz.ktandroidcomposesimple.view.module.home.HomeViewModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: HomeViewModule = hiltViewModel()
) {

    val titles = viewModel.viewStates.titles

   val uiController =  rememberSystemUiController()

    val pagerState = rememberPagerState(initialPage = viewModel.pageIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val currentModifier = if(uiController.isStatusBarVisible){
            Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(0.35f))
                .statusBarsPadding()
        }else{

            Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(0.35f))
        }

        Box(modifier = currentModifier){

            Column() {
                TabRow(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = Color.Transparent,
                    indicator = @Composable { tabPositions ->

                        val currentTabPosition = tabPositions[pagerState.currentPage]
                        //修改指示器长度
                        val currentTabWidth by animateDpAsState(
                            targetValue = currentTabPosition.width / 5,
                            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                        )
                        //修改指示器偏移量为居中
                        val indicatorOffset by animateDpAsState(
                            targetValue = currentTabPosition.left + (currentTabPosition.width / 2 - currentTabWidth / 2),
                            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                        )
                        //自带的Indicator指示器，只需改Modifier就可以了
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.BottomStart)
                                .offset(x = indicatorOffset)
                                .width(currentTabWidth)
                                .padding(bottom = 10.dp)
                                //修改指示器高度为1dp，默认2dp
                                .height(1.dp),
                            color = Color.White
                        )
                    }
                )
                {
                    titles.forEachIndexed { index, tabTitle ->
                        Tab(selected = pagerState.currentPage == index,
                            onClick = {

                                if (pagerState.currentPage != index){
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Log.i("KT","changeIndex$index,lastVue${pagerState.currentPage}")
                                        pagerState.scrollToPage(index)
                                    }
                                }

                            },
                            text = {
                                Text(
                                    text = tabTitle.text,
                                    style = if (pagerState.currentPage == index)
                                        MaterialTheme.typography.bodyLarge
                                    else
                                        MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        )
                    }
                }
            }
        }

        HorizontalPager(
            count = titles.size,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> WanAndroidSquare(navCtrl, scaffoldState)
                1 -> WanAndroidRecommendPage(navCtrl, scaffoldState)
                2 -> QuestionPage(navCtrl, scaffoldState)
            }
        }



    }
}




