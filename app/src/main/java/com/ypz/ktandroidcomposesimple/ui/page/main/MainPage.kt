package com.ypz.ktandroidcomposesimple.ui.page.main

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.WebData
import com.ypz.ktandroidcomposesimple.tools.fromJson
import com.ypz.ktandroidcomposesimple.tools.toJson
import com.ypz.ktandroidcomposesimple.ui.page.home.HomePage
import com.ypz.ktandroidcomposesimple.ui.page.person.PersonCenterPage
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush
import com.ypz.ktandroidcomposesimple.ui.weidget.MainBottomNavBarView
import com.ypz.ktandroidcomposesimple.R
import com.ypz.ktandroidcomposesimple.tools.RouteTools
import com.ypz.ktandroidcomposesimple.ui.page.collect.CollectPage
import com.ypz.ktandroidcomposesimple.ui.page.login.LoginPage
import com.ypz.ktandroidcomposesimple.ui.page.search.SearchPage
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush2
import com.ypz.ktandroidcomposesimple.ui.webview.WebViewPage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainPage(navCtrl: NavHostController, scaffoldState: ScaffoldState) {

    Log.i("ypz", "llllllllll")
//    val navCtrl = rememberNavController()
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
//    val scaffoldState = rememberScaffoldState()

    val bottomBaseColor = Color.Transparent

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier.background(
                    brush = Brush.radialGradient(
                        listOf(
                            bottomBaseColor.copy(alpha = 0.65f),
                            bottomBaseColor.copy(alpha = 0.35f),
                        ),
                        radius = 0.01f
                    )
                )
            ) {
                when (currentDestination?.route) {
                    AppRouter.HOME -> MainBottomNavBarView(navCtrl = navCtrl)
                    AppRouter.COLLECTION -> MainBottomNavBarView(navCtrl = navCtrl)
                    AppRouter.PERSION_CENTER -> MainBottomNavBarView(navCtrl = navCtrl)
                }
            }
        },
        floatingActionButton = {

            when (currentDestination?.route) {
                AppRouter.HOME -> Card(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    elevation = elevatedCardElevation(20.dp),
                    shape = RoundedCornerShape(36.dp),
                    border = BorderStroke(1.5.dp, BaseBrush2())
                ) {
                    Image(
                        painter = rememberVectorPainter(image = Icons.Default.Search),
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = "searchImage",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp, 36.dp)
                            .clip(RoundedCornerShape(36.dp))
                            .padding(8.dp)
                            .clickable {
                                RouteTools.navTo(navCtrl, AppRouter.ARTICLE_SEARCH + "/111")
                            },
                    )
                }
            }

        },
        content = {


            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = BaseBrush())
                    .padding(bottom =  it.calculateBottomPadding()),
                navController = navCtrl,
                startDestination = AppRouter.HOME
            ) {

                //首页
                composable(route = AppRouter.HOME) {
                    HomePage(navCtrl = navCtrl, scaffoldState = scaffoldState)
                }

                //收藏
                composable(route = AppRouter.COLLECTION) {
                    CollectPage(navCtrl, scaffoldState)
                }

                //我的
                composable(route = AppRouter.PERSION_CENTER) {
                    PersonCenterPage(navCtrl, scaffoldState)
                }

                //WebView
                composable(
                    route = AppRouter.WEB_VIEW + "/{webData}",
                    arguments = listOf(navArgument("webData") { type = NavType.StringType })
                ) {
                    val args = it.arguments?.getString("webData")?.fromJson<WebData>()
                    if (args != null) {
                        WebViewPage(webData = args, navCtrl = navCtrl)
                    }
                }


                //文章搜索页
                composable(
                    route = AppRouter.ARTICLE_SEARCH + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) {
                    SearchPage(navCtrl, scaffoldState)
                }

//
                //登录
                composable(route = AppRouter.LOGIN) {
                    LoginPage(navCtrl = navCtrl, scaffoldState = scaffoldState)
                }


            }

        }
    )


}