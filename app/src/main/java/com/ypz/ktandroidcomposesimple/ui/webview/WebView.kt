package com.ypz.ktandroidcomposesimple.ui.webview

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.WebData
import com.ypz.ktandroidcomposesimple.tools.RouteTools.back
import com.ypz.ktandroidcomposesimple.tools.SizeTools
import com.ypz.ktandroidcomposesimple.ui.theme.ToolBarHeight
import com.ypz.ktandroidcomposesimple.ui.weidget.AppToolsBar
import com.ypz.ktandroidcomposesimple.R

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WebViewPage(
    webData: WebData,
    navCtrl: NavHostController
) {
    var ctrl: WebViewCtrl? by remember { mutableStateOf(null) }
    Box(modifier = Modifier.statusBarsPadding()) {
        var isRefreshing: Boolean by remember { mutableStateOf(false) }
        val refreshState = rememberSwipeRefreshState(isRefreshing)
        AndroidView(
            modifier = Modifier
                .padding(top = ToolBarHeight + 5.dp)
                .fillMaxSize(),
            factory = { context ->
                FrameLayout(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    val progressView = ProgressBar(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            SizeTools.dp2px(2f)
                        )
                        progressDrawable =
                            context.resources.getDrawable(R.drawable.horizontal_progressbar)
                        indeterminateTintList =
                            ColorStateList.valueOf(context.resources.getColor(R.color.teal_200))
                    }
                    val webView = WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                    addView(webView)
                    addView(progressView)
                    ctrl = WebViewCtrl(this, webData.url, onWebCall = { isFinish ->
                        isRefreshing = !isFinish
                    })
                    ctrl?.initSettings()
                }

            },
            update = {

            }
        )

        AppToolsBar(title = webData.title ?: "标题", onBack = {
            ctrl?.onDestroy()
            navCtrl.back()
        })
    }
}