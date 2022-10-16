package com.ypz.ktandroidcomposesimple

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ypz.ktandroidcomposesimple.ui.PageNavRouter
import com.ypz.ktandroidcomposesimple.ui.PageType
import com.ypz.ktandroidcomposesimple.ui.page.SplashPage
import com.ypz.ktandroidcomposesimple.ui.page.login.LoginPage
import com.ypz.ktandroidcomposesimple.ui.page.main.MainPage
import com.ypz.ktandroidcomposesimple.ui.router.AppRouter
import com.ypz.ktandroidcomposesimple.ui.theme.BaseBrush
import com.ypz.ktandroidcomposesimple.ui.theme.KTAndroidComposeSimpleTheme
import com.ypz.ktandroidcomposesimple.ui.weidget.LabelTextButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { navStartPage() }

    }

}

@Composable
fun navStartPage() {

    val pageType = PageNavRouter.instance.currentPageType.observeAsState()

    KTAndroidComposeSimpleTheme {

        val navCtrl = rememberNavController()
        val scaffoldState = rememberScaffoldState()

//        Log.i("ypz","llllllllll")
        Crossfade(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = BaseBrush()),
            targetState = pageType,
//            animationSpec = SpringSpec( dampingRatio =  DampingRatioHighBouncy, stiffness = StiffnessVeryLow)
            animationSpec = SnapSpec(50)
        ) { currentPageType ->

            when (currentPageType.value) {
                PageType.SplashPageType -> {
//                    Log.i("ypz","llllllllllS")
                    SplashPage()
                }
                PageType.MainPageType -> {
//                    Log.i("ypz","llllllllllM")
                    MainPage(navCtrl, scaffoldState)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultSpalshPreview() {
    KTAndroidComposeSimpleTheme {
        SplashPage()
    }
}