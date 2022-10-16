package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.*
import com.ypz.ktandroidcomposesimple.R



@Composable
fun Loader(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
            .size(100.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingLoading(modifier: Modifier) {

    Card(
        modifier = modifier
            .fillMaxWidth(0.65f)
            .height(180.dp),
        containerColor = Color.White,
        contentColor = Color.White,
    ) {

        ConstraintLayout(Modifier.fillMaxSize()) {

            val topLottie = createRef()
            val bottomLottie = createRef()

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.refresh))

            LottieAnimation(
                composition,
                speed = 2f,
                contentScale = ContentScale.FillHeight,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.constrainAs(topLottie){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }.fillMaxSize()
            )

            val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
            LottieAnimation(
                composition2,
                speed = 2f,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .constrainAs(bottomLottie){
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutLoading(modifier: Modifier) {

    Card(
        modifier = modifier
            .fillMaxWidth(0.65f)
            .height(180.dp),
        containerColor = Color.White,
        contentColor = Color.White,
    ) {

        ConstraintLayout(Modifier.fillMaxSize()) {

            val topLottie = createRef()
            val bottomLottie = createRef()

            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("AndroidWave.json"))

            LottieAnimation(
                composition,
                speed = 2f,enableMergePaths = true,
                contentScale = ContentScale.FillHeight,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.constrainAs(topLottie){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }.fillMaxSize()
            )

            val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
            LottieAnimation(
                composition2,
                speed = 2f,
                iterations = LottieConstants.IterateForever,
                enableMergePaths = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .constrainAs(bottomLottie){
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
            )
        }


    }
}

private fun lerp(valueA: Float, valueB: Float, progress: Float): Float {
    val smallerY = minOf(valueA, valueB)
    val largerY = maxOf(valueA, valueB)
    return smallerY + progress * (largerY - smallerY)
}