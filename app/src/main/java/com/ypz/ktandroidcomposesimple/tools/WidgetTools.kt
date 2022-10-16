package com.ypz.ktandroidcomposesimple.tools

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import java.util.Objects

const val VIEW_CLICK_INTERVAL_TIME = 800L//View的click方法的两次点击间隔时间

@Composable
inline fun Modifier.singleClick(
    time: Long = VIEW_CLICK_INTERVAL_TIME,
    enabled: Boolean = true,//中间这三个是clickable自带的参数
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableStateOf(value = -1L) }//使用remember函数记录上次点击的时间    return clickable(enabled, onClickLabel, role) {

    return clickable {
        val currentTimeMillis = System.currentTimeMillis()

        if(currentTimeMillis - time >= lastClickTime && (lastClickTime == -1L)){
            lastClickTime =  currentTimeMillis
            return@clickable
        }

        if (currentTimeMillis - time >= lastClickTime && (lastClickTime != -1L)) {//判断点击间隔,如果在间隔内则不回调
            onClick()
            lastClickTime = currentTimeMillis
            return@clickable
        }
    }
}
