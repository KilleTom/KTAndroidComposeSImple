package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ypz.ktandroidcomposesimple.R
import kotlinx.coroutines.delay

/**
 * 轮播图
 * [looperTime] 停留时间
 * [loadImageRes] 加载中显示的布局
 * [indicatorLocation] 指示点的的位置,默认是轮播图下方的中间,带一点padding
 * [itemClickAction] 轮播图点击事件
 */
@ExperimentalPagerApi
@Composable
fun Banner(
    list: List<BannerData>?,
    looperTime: Long = 3000,
    indicatorLocation: Alignment = Alignment.BottomCenter,
    itemClickAction: (link: String, title: String) -> Unit
) {

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(16.dp)
            .height(120.dp)
    ) {


        if (list == null) {
            //加载中的图片
            Image(
                Icons.Outlined.ThumbUp,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(16.dp)),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            val pagerState = rememberPagerState(
                //初始页面
                initialPage = 0
            )

            //监听动画执行
            var executeChangePage by remember { mutableStateOf(false) }
            var currentPageIndex = 0

            //自动滚动
            LaunchedEffect(pagerState.currentPage, executeChangePage) {
                if (pagerState.pageCount > 0) {
                    delay(looperTime)
                    //这里直接+1就可以循环，前提是infiniteLoop == true
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % (pagerState.pageCount))
                }
            }

            HorizontalPager(
                count = list.size,
                state = pagerState,
                modifier = Modifier
                    .pointerInput(pagerState.currentPage) {
                        awaitPointerEventScope {
                            while (true) {
                                //核心思想先处理手势时间
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                val dragEvent = event.changes.firstOrNull()

                                when {
                                    //当前移动手势是否已被消费
                                    dragEvent!!.positionChangeConsumed() -> {
                                        return@awaitPointerEventScope
                                    }
                                    //处理按下且未消费的touch事件
                                    dragEvent.changedToDownIgnoreConsumed() -> {
                                        currentPageIndex = pagerState.currentPage
                                    }
                                    //处理手指抬起并未消费过的事件
                                    dragEvent.changedToUpIgnoreConsumed() -> {
                                        //判断是否为点击事件
                                        if (pagerState.targetPage == null) return@awaitPointerEventScope
                                        //事件没有被消费触发动画
                                        if (currentPageIndex == pagerState.currentPage && pagerState.pageCount > 1) {
                                            executeChangePage = !executeChangePage
                                        }
                                    }
                                }
                            }
                        }
                    }
                    .clickable {
                        with(list[pagerState.currentPage]) {
                            itemClickAction.invoke(linkUrl, title)
                        }
                    }
                    .fillMaxSize(),
            ) { page ->
                Image(
                    painter = rememberCoilPainter(list[page].imageUrl),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .align(indicatorLocation)
                    .padding(bottom = 6.dp, start = 6.dp, end = 6.dp)
            ) {

                //指示点
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in list.indices) {
                        //大小
                        var size by remember { mutableStateOf(5.dp) }
                        size = if (pagerState.currentPage == i) 7.dp else 5.dp

                        //颜色
                        val color =
                            if (pagerState.currentPage == i) MaterialTheme.colors.primary else Color.Gray

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color)
                                //当size改变的时候以动画的形式改变
                                .animateContentSize()
                                .size(size)
                        )
                        //指示点间的间隔
                        if (i != list.lastIndex) Spacer(
                            modifier = Modifier
                                .height(0.dp)
                                .width(4.dp)
                        )
                    }
                }

            }
        }

    }

}

/**
 * 轮播图数据
 */
data class BannerData(
    val title: String,
    val imageUrl: String,
    val linkUrl: String
)