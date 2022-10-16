package com.ypz.ktandroidcomposesimple.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp

@Composable
fun BaseBrush(): Brush = Brush.linearGradient(
    colors = if (isSystemInDarkTheme()) {
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
        )
    },
    tileMode = TileMode.Mirror
)

@Composable
fun BaseBrush2(): Brush = Brush.horizontalGradient(
    colors = if (isSystemInDarkTheme()) {
        listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.primary,
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(0.85f),
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.tertiary.copy(0.85f)
        )
    },
    tileMode = TileMode.Mirror
)


fun Modifier.BaseFullStatusTitleModifier(): Modifier {

    val baseColor = Color.Transparent
    return this
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(bottom = 10.dp)
//        .blur(32.dp)
        .background(
            brush = Brush.radialGradient(
                listOf(
                    baseColor.copy(alpha = 0.18f),
                    baseColor.copy(alpha = 0.09f),
                ),
                radius = 0.01f
            )
        )
}