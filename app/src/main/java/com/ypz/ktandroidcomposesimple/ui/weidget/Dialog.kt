package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ypz.ktandroidcomposesimple.R
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush2


@Composable
fun SampleAlertDialog(
    title: String,
    content: String,
    cancelText: String = " Cancle ",
    confirmText: String = " Confirm ",
    onConfirmClick: () -> Unit,
    //onCancelClick: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
        backgroundColor = Color.Transparent.copy(0.35f),
        title = {
            MediumTitle(title = title,color = Color.White)
        },
        text = {
            TextContent(text = content,color = Color.White)
        },
        onDismissRequest = onDismiss,
        confirmButton = {

            Card(
                backgroundColor = Color.LightGray.copy(0.35f),
                modifier = Modifier
                    .clickable {
                        onDismiss.invoke()
                        onConfirmClick.invoke()
                    }
                    .padding(end = 20.dp, bottom = 20.dp),
                elevation = 0.dp
            ) {
                TextContent(
                    text = confirmText, color = Color.White, modifier = Modifier.padding(
                        5.dp
                    )
                )
            }

        },
        dismissButton = {
            Card(
                backgroundColor = Color.LightGray.copy(0.45f),
                modifier = Modifier
                    .clickable {
                        onDismiss.invoke()
                    }
                    .padding(end = 10.dp, bottom = 20.dp),
                elevation = 0.dp
            ) {
                TextContent(
                    text = cancelText, color = Color.White, modifier = Modifier.padding(
                        5.dp
                    )
                )
            }
        },
        modifier = Modifier
            .background(brush = BaseBrush2(), shape = RoundedCornerShape(16.0.dp))
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

@Composable
fun SelectAlertDialog(
    title: String,
    content: String,
    primaryButtonText: String,
    secondButtonText: String,
    onPrimaryButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card() {
            Column(Modifier.padding(20.dp)) {
                MediumTitle(title = title, modifier = Modifier.padding(bottom = 20.dp))
                TextContent(text = content, modifier = Modifier.padding(bottom = 20.dp))
                PrimaryButton(text = primaryButtonText, Modifier.padding(bottom = 10.dp)) {
                    onPrimaryButtonClick.invoke()
                    onDismiss.invoke()
                }
                SecondlyButton(text = secondButtonText) {
                    onSecondButtonClick.invoke()
                    onDismiss.invoke()
                }
            }
        }
    }
}

@Composable
fun InfoDialog(
    title: String = "关于我",
    vararg content: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true),
        title = {
            MediumTitle(title = title)
        },
        text = {
            Column(
                Modifier.defaultMinSize(minWidth = 300.dp)
            ) {
                content.forEach {
                    TextContent(
                        text = it,
                        modifier = Modifier.padding(bottom = 10.dp),
                        canCopy = true
                    )
                }
            }
        },
        confirmButton = {
            TextContent(
                text = "关闭",
                modifier = Modifier
                    .padding(end = 18.dp, bottom = 18.dp)
                    .clickable { onDismiss.invoke() }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoggingtOutDialog() {
    Dialog({}) {
        Card(
            modifier = Modifier
                .background(brush = BaseBrush(), shape = RoundedCornerShape(16.0.dp))
                .fillMaxWidth()
                .height(180.dp),
            backgroundColor = Color.Transparent.copy(0.35f),
            shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
            elevation = 0.dp
        ) {
            ConstraintLayout(Modifier.fillMaxSize()) {

                val topLottie = createRef()
//                val bottomLottie = createRef()

                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loggingout))

                LottieAnimation(
                    composition,
                    speed = 10f,
                    contentScale = ContentScale.FillHeight,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .constrainAs(topLottie) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                        .fillMaxSize()
                )

//                val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
//                LottieAnimation(
//                    composition2,
//                    speed = 2f,
//                    iterations = LottieConstants.IterateForever,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(80.dp)
//                        .constrainAs(bottomLottie) {
//                            start.linkTo(parent.start)
//                            bottom.linkTo(parent.bottom)
//                        }
//                        .fillMaxWidth()
//                )
            }
        }
    }
}