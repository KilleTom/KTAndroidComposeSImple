package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.material.placeholder
import com.ypz.ktandroidcomposesimple.ui.theme.ToolBarHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun TitleTipView(
    modifier: Modifier = Modifier,
    tagText: String,
    tagBgColor: Color = Color.Transparent,
    borderColor: Color = Color.White,
    tagTextColor: Color = Color.White,
    isLoading: Boolean = false,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(color = tagBgColor)
            .clip(RoundedCornerShape(2.dp))
            .border(width = 1.dp, color = borderColor)
            .placeholder(
                visible = isLoading,
            )
    ) {
        MiniTitle(
            text = tagText,
            color = tagTextColor,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 3.dp, vertical = 1.dp),
        )
    }
}

data class TabTitle(
    val id: Int,
    val text: String,
    var cachePosition: Int = 0,
    var selected: Boolean = false
)

/**
 * 普通标题栏头部
 */
@Composable
fun AppToolsBar(
    title: String,
    rightText: String? = null,
    onBack: (() -> Unit)? = null,
    onRightClick: (() -> Unit)? = null,
    imageVector: ImageVector? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ToolBarHeight)
            .background(Color.Transparent)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            if (onBack != null) {
                Icon(
                    Icons.Default.ArrowBack,
                    null,
                    Modifier
                        .clickable(onClick = onBack)
                        .align(Alignment.CenterVertically)
                        .padding(12.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (!rightText.isNullOrEmpty() && imageVector == null) {


                TextContent(
                    text = rightText,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 20.dp)
                        .clickable { onRightClick?.invoke() }
                )
            }

            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 12.dp)
                        .clickable {
                            onRightClick?.invoke()
                        })
            }
        }

        androidx.compose.material3.Text(
            text = title,
            color = Color.White,
            fontFamily = FontFamily.Cursive,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.W900,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 40.dp)
        )

    }
}

fun popupSnackBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    label: String,
    message: String,
    onDismissCallback: () -> Unit = {}
) {
    scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(actionLabel = label, message = message)
        onDismissCallback.invoke()
    }

}