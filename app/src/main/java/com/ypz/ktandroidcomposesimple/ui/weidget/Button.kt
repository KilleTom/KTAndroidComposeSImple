package com.ypz.ktandroidcomposesimple.ui.weidget

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.material.placeholder
import com.ypz.ktandroidcomposesimple.tools.VIEW_CLICK_INTERVAL_TIME
import com.ypz.ktandroidcomposesimple.tools.singleClick
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush
import com.ypz.ktandroidcomposesimple.ui.theme.buttonCorner
import com.ypz.ktandroidcomposesimple.ui.theme.buttonHeight
import org.jetbrains.annotations.NotNull

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    brush: Brush = BaseBrush(),
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonRadius: Dp = buttonCorner,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .background(brush = brush, shape = RoundedCornerShape(buttonRadius))
            .clickable { onClick() }
    ) {
        TextContent(text = text, color = textColor, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun AppEasyButton(
    text: String,
    modifier: Modifier = Modifier,
    fillWidthStatus:Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) {


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            color = textColor,
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
fun AppButtonV2(
    text: String,
    clickInvalidTime: Long = VIEW_CLICK_INTERVAL_TIME,
    modifier: Modifier = Modifier,
    brush: Brush = BaseBrush(),
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonRadius: Dp = buttonCorner,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(buttonHeight)
            .background(brush = brush, shape = RoundedCornerShape(buttonRadius))
            .clickable {
                onClick?.invoke()
            }

    ) {
        Text(
            text = text,
            color = textColor,
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
fun AppButtonMini(
    text: String,
    modifier: Modifier = Modifier,
    brush: Brush = BaseBrush(),
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonRadius: Dp = buttonCorner,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(brush = brush, shape = RoundedCornerShape(buttonRadius))
            .clickable {
                onClick.invoke()
            }

    ) {
        Text(
            text = text,
            color = textColor,
            fontFamily = FontFamily.Cursive,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Justify,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.W900,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(3.dp)
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelTextButton(
    @NotNull text: String,
    modifier: Modifier = Modifier,
    brush :Brush= BaseBrush(),
    isSelect: Boolean = true,
    specTextColor: Color? = null,
    cornerValue: Dp = 25.dp / 2,
    isLoading: Boolean = false,
    replaceBackgroundStatus:Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {


    Text(
        text = text,
        modifier = modifier
            .height(25.dp)
            .wrapContentWidth()
            .clip(shape = RoundedCornerShape(cornerValue))
            .background(brush = BaseBrush())
            .padding(
                horizontal = 10.dp,
                vertical = 3.dp
            )
            .combinedClickable(
                enabled = !isLoading,
                onClick = { onClick?.invoke() },
                onLongClick = { onLongClick?.invoke() }
            )
            .placeholder(
                visible = isLoading,
                color = MaterialTheme.colorScheme.inversePrimary
            ),
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        color = specTextColor ?: Color.White,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelTextButton2(
    @NotNull text: String,
    modifier: Modifier = Modifier,
    isSelect: Boolean = true,
    specTextColor: Color? = null,
    cornerValue: Dp = 25.dp / 2,
    isLoading: Boolean = false,
    replaceBackgroundStatus:Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {


    Text(
        text = text,
        modifier = modifier
            .height(25.dp)
            .wrapContentWidth()
            .clip(shape = RoundedCornerShape(cornerValue))
            .padding(
                horizontal = 10.dp,
                vertical = 3.dp
            )
            .combinedClickable(
                enabled = !isLoading,
                onClick = { onClick?.invoke() },
                onLongClick = { onLongClick?.invoke() }
            )
            .placeholder(
                visible = isLoading,
                color = MaterialTheme.colorScheme.inversePrimary
            ),
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        color = specTextColor ?: Color.White,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AppButton(
        text = text,
        modifier = modifier,
        textColor = Color.White,
        onClick = onClick,
    )
}

@Composable
fun SecondlyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AppButton(
        text = text,
        modifier = modifier,
        textColor = Color.White.copy(0.88f),
        onClick = onClick
    )
}
