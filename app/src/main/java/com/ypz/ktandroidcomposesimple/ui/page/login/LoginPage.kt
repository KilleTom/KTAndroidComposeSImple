package com.ypz.ktandroidcomposesimple.ui.page.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ypz.ktandroidcomposesimple.tools.showToast
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush
import com.ypz.ktandroidcomposesimple.ui.weidget.*
import com.ypz.ktandroidcomposesimple.view.module.login.LoginViewAction
import com.ypz.ktandroidcomposesimple.view.module.login.LoginViewEvent
import com.ypz.ktandroidcomposesimple.view.module.login.LoginViewModule

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun LoginPage(
    viewModel: LoginViewModule = hiltViewModel(),
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val viewStates = viewModel.viewStates
    val coroutineState = rememberCoroutineScope()

    val showLoadingStatus = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is LoginViewEvent.PopBack) {
                keyboardController?.hide()
                navCtrl.popBackStack()
                showLoadingStatus.value = false
                return@collect
            }
            if (it is LoginViewEvent.ErrorMessage) {
                keyboardController?.hide()
                showToast(it.message?:"error")
                popupSnackBar(coroutineState, scaffoldState, label = "Login Error", it.message)
                showLoadingStatus.value = false
                return@collect
            }
            if(it is LoginViewEvent.Logging){
                keyboardController?.hide()
                showLoadingStatus.value = true
                return@collect
            }
        }


    }

    BackHandler(enabled = true) {
        if(!showLoadingStatus.value){
            viewModel.dispatch(LoginViewAction.PopBack)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBrush())
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        keyboardController?.hide()
                    }
                )
            },
//            .blur(10.dp, 20.dp),
    ) {

        val title = createRef()
        val loginCardLayout = createRef()
        val backIcon = createRef()

        val loadingView = createRef()

        createVerticalChain(title, loginCardLayout, chainStyle = ChainStyle.Packed(0.335f))

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(bottom = 40.dp),
            text = "learning Android",
            color = Color.White,
            fontFamily = FontFamily.Cursive,
            fontSize = 36.sp,
            textAlign = TextAlign.Justify,
            letterSpacing = 0.5.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.W900,
        )

        val bottomBaseColor = Color.Transparent

        Card(
            Modifier
                .constrainAs(loginCardLayout) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.85f),
            containerColor = Color.Transparent
        ) {

            ConstraintLayout(Modifier.fillMaxWidth()) {

                val bg = createRef()
                val content = createRef()

                Box(modifier = Modifier
                    .constrainAs(bg) {
                        start.linkTo(content.start)
                        end.linkTo(content.end)
                        top.linkTo(content.top)
                        bottom.linkTo(content.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }.background(
                        brush = Brush.radialGradient(
                            listOf(
                                bottomBaseColor.copy(alpha = 0.35f),
                                bottomBaseColor.copy(alpha = 0.25f),
                            ),
                            radius = 6.0f
                        ),
                        shape = RoundedCornerShape(28.dp),
                    )
                    .blur(32.dp))

                Column(
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(content) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }) {

                    LoginNameEditView(
                        text = viewStates.account,
                        labelText = "account",
                        hintText = "Inputting account",
                        onValueChanged = { viewModel.dispatch(LoginViewAction.UpdateAccount(it)) },
                        onRightIconClick = { viewModel.dispatch(LoginViewAction.ClearAccount) },
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        controlColor = Color.White
                    )

                    LoginPasswordEditView(
                        text = viewStates.password,
                        labelText = "password",
                        hintText = "Inputting password",
                        onValueChanged = { viewModel.dispatch(LoginViewAction.UpdatePassword(it)) },
                        controlColor = Color.White
                    )

                    AppButtonV2(
                        text = "l o g i n",
                        textColor = Color.White,
                        brush = Brush.radialGradient(
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
                            radius = 0.05f
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp, horizontal = 20.dp)
                            .fillMaxWidth()
                    ) {

                        viewModel.dispatch(LoginViewAction.Login)
                    }

                }
            }


        }


        Box(
            modifier = Modifier
                .constrainAs(backIcon) {
                    start.linkTo(parent.start, margin = 20.dp)
                    top.linkTo(parent.top, margin = 26.dp)
                }
                .size(36.dp, 36.dp)
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(36.dp)
                )
        ) {
            androidx.compose.material.Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        viewModel.dispatch(LoginViewAction.PopBack)
                    }
                    .padding(8.dp)
            )
        }

        if (showLoadingStatus.value){

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(0.35f))){
                LogoutLoading(Modifier.align(Alignment.Center))
            }
        }
    }

}