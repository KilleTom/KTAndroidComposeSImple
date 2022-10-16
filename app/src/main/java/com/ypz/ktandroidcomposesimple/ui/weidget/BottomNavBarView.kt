package com.ypz.ktandroidcomposesimple.ui.weidget

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.navigationBarsPadding
import com.ypz.ktandroidcomposesimple.R
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter


sealed class BaseBottomNavRoute(
    var routeName: String,
    @StringRes var stringId: Int,
    var icon: ImageVector
) {
    object Home : BaseBottomNavRoute(AppRouter.HOME, R.string.home, Icons.Default.Home)
    object Collection :
        BaseBottomNavRoute(AppRouter.COLLECTION, R.string.collection, Icons.Default.Favorite)

    object Profile :
        BaseBottomNavRoute(AppRouter.PERSION_CENTER, R.string.profile, Icons.Default.Person)
}

@Composable
fun MainBottomNavBarView(navCtrl: NavHostController) {

    val bottomNavList = listOf(
        BaseBottomNavRoute.Home,
        BaseBottomNavRoute.Collection,
        BaseBottomNavRoute.Profile
    )

    BottomNavigation(
        modifier = Modifier
            .navigationBarsPadding(),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavList.forEach { screen ->

            val selected =
                currentDestination?.hierarchy?.any { it.route == screen.routeName } == true

            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        tint = if (selected) Color.White else Color.LightGray,
                        modifier = if (selected) Modifier.padding(2.dp) else Modifier.padding(1.dp)
                    )
                },
                selectedContentColor = Color.White,
                label = {
                    Text(
                        text = stringResource(screen.stringId),
                        color = if (selected) Color.White else Color.LightGray,
                        modifier = Modifier.padding(3.dp),
                        fontFamily = FontFamily.Cursive,
                        textAlign = TextAlign.Justify,
                        fontStyle = FontStyle.Italic,
                    )
                },
                selected = selected,
                onClick = {
//                    println("BottomNavView当前路由 ===> ${currentDestination?.hierarchy?.toList()}")
//                    println("当前路由栈 ===> ${navCtrl.graph.nodes}")
                    println("routerName${screen.routeName}")
                    if (currentDestination?.route != screen.routeName) {
                        navCtrl.navigate(screen.routeName) {
                            popUpTo(navCtrl.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}

