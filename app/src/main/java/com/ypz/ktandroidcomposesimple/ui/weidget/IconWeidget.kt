package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.BlurTransformation
import com.google.accompanist.placeholder.material.placeholder
import com.ypz.ktandroidcomposesimple.R
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush2

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HotIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_hot),
        contentDescription = null,
        tint = Color.White,
        modifier = modifier
            .size(20.dp)
            .pointerInteropFilter { false }
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_share),
        contentDescription = null,
        modifier = modifier
            .width(25.dp)
            .height(25.dp)
            .pointerInteropFilter { false }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FavouriteIcon(
    modifier: Modifier = Modifier,
    isFavourite: Boolean = false,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    Icon(
        imageVector = if (isFavourite && !isLoading) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = null,
        tint =  MaterialTheme.colorScheme.primary,
        modifier = modifier
            .width(25.dp)
            .height(25.dp)
            .clickable(enabled = !isLoading) { onClick.invoke() }
            .pointerInteropFilter { false }
    )
}

@Composable
fun TimerIcon(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_time),
        contentDescription = "",
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .width(15.dp)
            .height(15.dp)
            .clip(RoundedCornerShape(15.dp / 2))
            .placeholder(
                visible = isLoading,
                color = MaterialTheme.colorScheme.inversePrimary
            )
    )
}

@Composable
fun UserIcon(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    accountUrl:String? = null,
    roundedCornerRadius: Dp = 15.dp
) {
    Icon(
        painter = rememberImagePainter(
            data = accountUrl?:R.drawable.ic_default_account,
            builder = {
                placeholder(R.drawable.ic_default_account)//占位图
                crossfade(true)//淡出效果
                scale(Scale.FIT)
                transformations(BlurTransformation(LocalContext.current, 0.1f, 2f))//高斯效果
                error(R.drawable.ic_default_account)
            }),
        contentDescription = "",
        modifier = modifier
            .defaultMinSize(15.dp,15.dp)
            .clip(RoundedCornerShape(roundedCornerRadius))
            .background(Color.White)
            .border(1.dp, BaseBrush2(),RoundedCornerShape(roundedCornerRadius))
            .placeholder(
                visible = isLoading,
                color = Color.Transparent.copy(0.65f)
            ),
        tint = Color.Unspecified
    )
}

@Composable
fun AddIcon(
    modifier: Modifier,
    color: Color = Color.White
) {
    Icon(
        imageVector = Icons.Default.Add,
        contentDescription = null,
        tint = color,
        modifier = modifier
    )
}

@Composable
fun NotificationIcon(
    modifier: Modifier,
    tintColor: Color = Color.White
) {
    Icon(
        Icons.Default.Notifications,
        contentDescription = "New message",
        modifier = modifier,
        tint = tintColor
    )
}

@Composable
fun DotView(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "",
        modifier = modifier
            .size(10.dp)
            .background(color = Color.White, RoundedCornerShape(5.dp)),
        color = Color.White,
        textAlign = TextAlign.Center,
        maxLines = 1,
        fontSize = 5.sp
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .clickable {
                onClick.invoke()
            }
            .pointerInteropFilter { false }
    )
}