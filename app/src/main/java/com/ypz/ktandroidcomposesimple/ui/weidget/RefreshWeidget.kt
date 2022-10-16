package com.ypz.ktandroidcomposesimple.ui.weidget

import android.R
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import com.ypz.ktandroidcomposesimple.R as AppR


@Composable
internal fun rememberUpdatedSlingshot(
    offsetY: Float,
    maxOffsetY: Float,
    height: Int
): Slingshot {
    val offsetPercent = min(1f, offsetY / maxOffsetY)
    val adjustedPercent = max(offsetPercent - 0.4f, 0f) * 5 / 3
    val extraOffset = abs(offsetY) - maxOffsetY

    // Can accommodate custom start and slingshot distance here
    val slingshotDistance = maxOffsetY
    val tensionSlingshotPercent = max(
        0f, min(extraOffset, slingshotDistance * 2) / slingshotDistance
    )
    val tensionPercent = (
            (tensionSlingshotPercent / 4) -
                    (tensionSlingshotPercent / 4).pow(2)
            ) * 2
    val extraMove = slingshotDistance * tensionPercent * 2
    val targetY = height + ((slingshotDistance * offsetPercent) + extraMove).toInt()
    val offset = targetY - height
    val strokeStart = adjustedPercent * 0.8f

    val startTrim = 0f
    val endTrim = strokeStart.coerceAtMost(MaxProgressArc)

    val rotation = (-0.25f + 0.4f * adjustedPercent + tensionPercent * 2) * 0.5f
    val arrowScale = min(1f, adjustedPercent)

    return remember { Slingshot() }.apply {
        this.offset = offset
        this.startTrim = startTrim
        this.endTrim = endTrim
        this.rotation = rotation
        this.arrowScale = arrowScale
    }
}

@Stable
internal class Slingshot {
    var offset: Int by mutableStateOf(0)
    var startTrim: Float by mutableStateOf(0f)
    var endTrim: Float by mutableStateOf(0f)
    var rotation: Float by mutableStateOf(0f)
    var arrowScale: Float by mutableStateOf(0f)
}

internal const val MaxProgressArc = 0.8f
private const val CrossfadeDurationMs = 100


@Composable
fun RefreshWidgetIndicator(
    state: SwipeRefreshState,
    refreshTrigger: Dp,
    modifier: Modifier = Modifier,
    fade: Boolean = true,
    scale: Boolean = false,
    arrowEnabled: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    shape: Shape = RoundedCornerShape(45.dp,45.dp,60.dp,60.dp),
    refreshingOffset: Dp = 16.dp,
    elevation: Dp = 6.dp,
) {
    val adjustedElevation = when {
        state.isRefreshing -> elevation
        state.indicatorOffset > 0.5f -> elevation
        else -> 0.dp
    }
    val size = 68.dp

    val indicatorRefreshTrigger = with(LocalDensity.current) { refreshTrigger.toPx() }

    val indicatorHeight = with(LocalDensity.current) { size.roundToPx() }
    val refreshingOffsetPx = with(LocalDensity.current) { refreshingOffset.toPx() }

    val slingshot = rememberUpdatedSlingshot(
        offsetY = state.indicatorOffset,
        maxOffsetY = indicatorRefreshTrigger,
        height = indicatorHeight,
    )

    var offset by remember { mutableStateOf(0f) }

    // If the user is currently swiping, we use the 'slingshot' offset directly
    if (state.isSwipeInProgress) {
        offset = slingshot.offset.toFloat()
    }

    LaunchedEffect(state.isSwipeInProgress, state.isRefreshing) {
        // If there's no swipe currently in progress, animate to the correct resting position
        if (!state.isSwipeInProgress) {
            animate(
                initialValue = offset,
                targetValue = when {
                    state.isRefreshing -> indicatorHeight + refreshingOffsetPx
                    else -> 0f
                }
            ) { value, _ ->
                offset = value
            }
        }
    }

    Surface(
        modifier = modifier
            .size(size = size)
            .graphicsLayer {
                // Translate the indicator according to the slingshot
                translationY = offset - indicatorHeight

                val scaleFraction = if (scale && !state.isRefreshing) {
                    val progress = offset / indicatorRefreshTrigger.coerceAtLeast(1f)

                    // We use LinearOutSlowInEasing to speed up the scale in
                    LinearOutSlowInEasing
                        .transform(progress)
                        .coerceIn(0f, 1f)
                } else 1f

                scaleX = scaleFraction
                scaleY = scaleFraction
            },
//        shape = shape,
        color = backgroundColor,
        elevation = 0.dp
    ) {

        Crossfade(
            targetState = state.isRefreshing,
            animationSpec = tween(durationMillis = CrossfadeDurationMs)
        ) { refreshing ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {


                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.ypz.ktandroidcomposesimple.R.raw.refresh))
                LottieAnimation(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = modifier.size(size),
                    isPlaying = refreshing
                )

            }
        }
    }
}


@Composable
fun <T : Any> RefreshWidget(
    lazyPagingItems: LazyPagingItems<T>,
    isRefreshing: Boolean = false,
    modifier: Modifier = Modifier,
    onRefresh: (() -> Unit) = {},
    listState: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit,
) {
    val rememberSwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    //错误页
    val err = lazyPagingItems.loadState.refresh is LoadState.Error
    if (err) {
        ErrorContent { lazyPagingItems.retry() }
        return
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState,
        onRefresh = {
            onRefresh.invoke()
            lazyPagingItems.refresh()
        },
        modifier = modifier,
        indicator = {state, refreshTrigger ->RefreshWidgetIndicator(state, refreshTrigger)  }
    ) {
        //刷新状态
        rememberSwipeRefreshState.isRefreshing =
            ((lazyPagingItems.loadState.refresh is LoadState.Loading) || isRefreshing)
        //列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            //条目布局
            itemContent()
            //加载更多状态：加载中和加载错误,没有更多
            if (!rememberSwipeRefreshState.isRefreshing) {
                item {
                    lazyPagingItems.apply {
                        when (loadState.append) {
                            is LoadState.Loading -> LoadingItem()
                            is LoadState.Error -> ErrorItem { retry() }
                            is LoadState.NotLoading -> {
                                if (loadState.append.endOfPaginationReached) {
                                    NoMoreItem()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorContent(retry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(id = R.drawable.stat_notify_error),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            androidx.compose.material3.Text(
                text = " Request Error",
                color = Color.White,
                fontFamily = FontFamily.Cursive,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.W900,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            RetryButton(
                onClick = { retry() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
fun RetryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .background(Color.Transparent.copy(0.45f), shape = RoundedCornerShape(18.68.dp))
            .padding(3.268.dp)
            .clickable { onClick() }
    ) {
        androidx.compose.material3.Text(
            text = " R e t r y ",
            color = Color.White,
            fontFamily = FontFamily.Cursive,
            fontSize = 18.sp,
            textAlign = TextAlign.Justify,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.W900,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ErrorItem(retry: () -> Unit) {
    RetryButton{retry()}
}

@Composable
fun NoMoreItem() {

    androidx.compose.material3.Text(
        text = " Not More",
        color = Color.White,
        fontFamily = FontFamily.Cursive,
        fontSize = 18.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W900,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )

}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), contentAlignment = Alignment.Center
    ) {

        Loader()

    }
}
