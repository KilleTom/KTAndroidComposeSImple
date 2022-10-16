package com.ypz.ktandroidcomposesimple.ui.page.person

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.ypz.ktandroidcomposesimple.ui.weidget.*
import com.ypz.ktandroidcomposesimple.R
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.WebData
import com.ypz.ktandroidcomposesimple.tools.RouteTools
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush2
import com.ypz.ktandroidcomposesimple.ui.theme.BaseFullStatusTitleModifier
import com.ypz.ktandroidcomposesimple.ui.theme.buttonHeight
import com.ypz.ktandroidcomposesimple.view.module.account.AccountViewAction
import com.ypz.ktandroidcomposesimple.view.module.account.AccountViewModule
import com.ypz.ktandroidcomposesimple.view.module.account.AccountViewState

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun PersonCenterPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    vm : AccountViewModule = hiltViewModel()
) {

    DisposableEffect(Unit) {
        vm.dispatch(AccountViewAction.OnStart)
        onDispose {}
    }

    val viewStates = vm.viewStates
    val isLogged = viewStates.isLogged
    val userInfo = viewStates.userInfo

    if (viewStates.showLogout) {
        SampleAlertDialog(
            title = "提示",
            content = "退出后，将无法查看我的文章、消息、收藏、积分、浏览记录等功能，确定退出登录吗？",
            onConfirmClick = {
                vm.dispatch(AccountViewAction.Logout)
            },
            onDismiss = {
                vm.dispatch(AccountViewAction.DismissLogoutDialog)
            }
        )
    }

    if (viewStates.showLoggingOut){
        LoggingtOutDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

        PersonHeadPage(vm.viewStates,navCtrl)

        if (isLogged) {

            userInfo?.let { info->

                Box(modifier = Modifier.weight(1f)) { PersonContent(navCtrl) }

                val bottomBaseColor = Color.Transparent

                Card(
                    Modifier
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    bottomBaseColor.copy(alpha = 0.35f),
                                    bottomBaseColor.copy(alpha = 0.25f),
                                ),
                                radius = 6.0f
                            ),
                            shape = RoundedCornerShape(28.dp),
                        )
//                        .blur(32.dp)
                        .fillMaxWidth(0.85f)
                        .height(buttonHeight)
                        .align(Alignment.CenterHorizontally),
                    containerColor = Color.Transparent,
                    ) {
                    AppEasyButton(
                        text = "l o g o u t",
                        textColor = Color.White,
                        modifier = Modifier
                            .background(
                                brush = BaseBrush2(),
                                shape = RoundedCornerShape(12.0.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    0.5.dp,
                                    Brush.radialGradient(
                                        listOf(
                                            bottomBaseColor.copy(alpha = 0.35f),
                                            bottomBaseColor.copy(alpha = 0.25f),
                                        ),
                                        radius = 6.0f
                                    )
                                ),
                                shape = RoundedCornerShape(12.0.dp)
                            )
                    ) {
                        vm.dispatch(AccountViewAction.ShowLogoutDialog)
                    }
                }

                Spacer(modifier = Modifier.padding(bottom = 20.dp))
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonHeadPage(viewStates: AccountViewState,navCtrl: NavHostController,) {

    val isLogged = viewStates.isLogged
    val userInfo = viewStates.userInfo

    Column(modifier = Modifier.BaseFullStatusTitleModifier()) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .statusBarsPadding()
        ) {

            val (icon, info, option) = createRefs()

            Image(
                painter =

                rememberImagePainter(
                    data = userInfo?.icon ?: "",
                    builder = {
                        placeholder(R.drawable.ic_default_account)//占位图
                        crossfade(true)//淡出效果
                        error(R.drawable.ic_default_account)
                        transformations(BlurTransformation(LocalContext.current, 0.1f, 2f))//高斯效果
                    }),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                    .width(56.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color.White)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
            )

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                    .constrainAs(info) {
                        start.linkTo(icon.end)
                        top.linkTo(icon.top)
                        bottom.linkTo(icon.bottom)
                    }
            ) {

                if (userInfo!=null) {

                    MainTitle(
                        title = userInfo.username?:"unknown userName",
                        color = Color.White,
                    )

                    MiniTitle(
                        text = "email: ${userInfo.email ?: "unknown userEmail"}",
                        color = Color.White,
                    )

//                    Row {
//                        TitleTipView(
//                            tagText = "Lv${userPoints?.level ?: "0"}",
//                            isLoading = false
//                        )
//                        TitleTipView(
//                            modifier = Modifier.padding(start = 3.dp),
//                            tagText = "积分${userPoints?.coinCount ?: "0"}",
//                            isLoading = false
//                        )
//                    }

                } else {

                    AppButtonMini(
                        text = " l o g i n ",
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
                            .wrapContentSize()
                    ) {
                        navCtrl.navigate(AppRouter.LOGIN)
                    }
                }

            }

        }

        PersonOptionsItem(
            onCollectClick = {
                try {
                    navCtrl.navigate(AppRouter.COLLECTION) {
                        popUpTo(navCtrl.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            },
            onMyShareClick = {
                //TODO
            },
            onHistoryClick = {
                //TODO
            },
            onRankingClick = {
                //TODO
            }
        )


    }

}

@Composable
private fun PersonOptionsItem(
    onCollectClick: () -> Unit,
    onMyShareClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onRankingClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 10.dp)
    ) {
        PresonOptionItem(
            modifier = Modifier.weight(1f),
            title = "我的收藏",
            iconRes = Icons.Default.FavoriteBorder
        ) {
            onCollectClick.invoke()
        }
        PresonOptionItem(
            modifier = Modifier.weight(1f),
            title = "我的文章",
            iconRes = painterResource(com.ypz.ktandroidcomposesimple.R.drawable.ic_article)
        ) {
            onMyShareClick.invoke()
        }
        PresonOptionItem(
            modifier = Modifier.weight(1f),
            title = "历史浏览",
            iconRes = painterResource(com.ypz.ktandroidcomposesimple.R.drawable.ic_history_record)
        ) {
            onHistoryClick.invoke()
        }
        PresonOptionItem(
            modifier = Modifier.weight(1f),
            title = "积分排行",
            iconRes = painterResource(com.ypz.ktandroidcomposesimple.R.drawable.ic_ranking)
        ) {
            onRankingClick.invoke()
        }
    }
}

@Composable
private fun PresonOptionItem(
    modifier: Modifier,
    title: String,
    iconRes: Any,
    onClick: () -> Unit
) {
    Column(modifier = modifier.clickable { onClick() }) {
        when (iconRes) {
            is Painter -> {
                Icon(
                    painter = iconRes,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            is ImageVector -> {
                Icon(
                    imageVector = iconRes,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        MiniTitle(
            text = title,
            color = Color.White,
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.CenterHorizontally)
        )
    }

}

//基本操作
@Composable
fun PersonContent(navCtrl: NavHostController) {

    LazyColumn(
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        item {
            ArrowRightListItem(
                iconRes = painterResource(R.drawable.ic_message),
                title = "消息",
                msgCount = 0
            ) {
                //TODO
            }
        }

        item {
            ArrowRightListItem(
                iconRes = painterResource(R.drawable.ic_feedback),
                title = "WanAndroid"
            ) {
            RouteTools.navTo(
                navCtrl = navCtrl,
                destinationName = AppRouter.WEB_VIEW,
                args = WebData(title = "官方网站", url = "https://www.wanandroid.com/index")
            )
            }
        }

        item {
            ArrowRightListItem(
                iconRes = painterResource(R.drawable.ic_data),
                title = "积分规则"
            ) {
            RouteTools.navTo(
                navCtrl = navCtrl,
                destinationName = AppRouter.WEB_VIEW,
                args = WebData(title = "积分规则", url = "https://www.wanandroid.com/blog/show/2653")
            )
            }
        }

    }
}

@Composable
fun LogOutPage(){

}