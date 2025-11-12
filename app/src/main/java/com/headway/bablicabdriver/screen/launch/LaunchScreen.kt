package com.headway.bablicabdriver.screen.launch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import kotlinx.coroutines.delay

@Composable
fun LaunchScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)

    LaunchedEffect(true) {
        var destination = ""

        val showIntro = sharedPreferenceManager.getShowIntro()
        Log.d("msg","showIntro: $showIntro")
        if (showIntro) {
            destination = Routes.IntroScreen.route
        } else {
            if(sharedPreferenceManager.getIsLogin()) {
                destination = Routes.DashboardScreen.route
            } else {
                destination = Routes.LoginScreen.route
            }
        }

        Log.d("msg","getIsLogin: ${sharedPreferenceManager.getIsLogin()}")
        delay(1000)
        navHostController.navigate(destination) {
            launchSingleTop = true
            popUpTo(Routes.LaunchScreen.route) {
                inclusive = true
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.drawable.ic_splash_logo),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .width(200.dp)
                    .height(122.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_buildings),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
            )

        }


    }

}