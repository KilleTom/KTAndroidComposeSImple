package com.ypz.ktandroidcomposesimple.ui.weidget

import android.icu.lang.UCharacter.LineBreak.H3
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material.placeholder
import com.ypz.ktandroidcomposesimple.ui.theme.ListTitleHeight

@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier,
    textStyle : TextStyle,
    color: Color = MaterialTheme.colorScheme.onSecondary,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLine: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Text(
        text = title,
        style = textStyle,
        modifier = modifier,
        color = color,
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
fun LargeTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        modifier = modifier,
        textStyle = MaterialTheme.typography.titleLarge,
        color = color ?: MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold,
        isLoading = isLoading
    )
}

@Composable
fun MainTitle(
    title: String,
    modifier: Modifier = Modifier,
    maxLine: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    isLoading: Boolean = false
) {
    Title(
        title = title,
        modifier = modifier,
        textStyle = MaterialTheme.typography.titleMedium,
        color = color,
        fontWeight = FontWeight.SemiBold,
        maxLine = maxLine,
        textAlign = textAlign,
        isLoading = isLoading
    )
}

@Composable
fun MediumTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false,
    textStyle :TextStyle= MaterialTheme.typography.titleMedium,
) {
    Title(
        title = title,
        textStyle = textStyle,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        isLoading = isLoading
    )
}

@Composable
fun MiniTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false
) {
    Title(
        title = text,
        modifier = modifier,
        textStyle = MaterialTheme.typography.labelSmall,
        color = color,
        maxLine = maxLines,
        textAlign = textAlign,
        isLoading = isLoading,
    )
}
@Composable
fun ListTitle(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String = "",
    isLoading: Boolean = false,
    onSubtitleClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .placeholder(false)
            .fillMaxWidth()
            .height(ListTitleHeight)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .width(5.dp)
                .height(16.dp)
                .align(Alignment.CenterVertically)
                .background(color = MaterialTheme.colorScheme.primary)
        )
        MediumTitle(
            title = title,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterVertically),
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.weight(1f))
        TextContent(
            text = subTitle,
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable {
                    onSubtitleClick.invoke()
                },
            isLoading = isLoading
        )
    }

}