package com.headway.bablicabdriver.screen.navgraph

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.screen.dashboard.DashboardScreen
import com.headway.bablicabdriver.screen.intro.IntroScreen
import com.headway.bablicabdriver.screen.launch.LaunchScreen
import com.headway.bablicabdriver.screen.login.LoginScreen
import com.headway.bablicabdriver.screen.login.OTPVerificationScreen
import com.headway.bablicabdriver.screen.login.WebScreen
import com.headway.bablicabdriver.screen.register.RegisterScreen
import com.headway.bablicabdriver.utils.composable2
import com.headway.bablicabdriver.utils.permissionhandler.rememberPermissionsState
import com.headway.bablicabdriver.viewmodel.MainViewModel


@Composable
fun NavigationGraph(
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current
    //navigation host
    val navHostController = rememberNavController()
    val startDestination = Routes.LaunchScreen.route

    val notificationPermission =
        rememberPermissionsState(permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.POST_NOTIFICATIONS)
        } else emptyList(),
            onGrantedAction = {},
            onDeniedAction = {},
            onPermanentlyDeniedAction = {}
        )

    LaunchedEffect(true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission.launchPermissionRequestsAndAction()
        }
    }


    Box {

        NavHost(
            navController = navHostController,
            startDestination = startDestination
        ) {
            composable2 (
                route = Routes.LaunchScreen.route,
                content = {
                    LaunchScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.IntroScreen.route,
                content = {
                    IntroScreen(
                        navHostController = navHostController,
                        mainViewModel = mainViewModel
                    )
                }
            )
            composable2 (
                route = Routes.LoginScreen.route,
                content = {
                    LoginScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.WebPageScreen.route,
                content = {
                    WebScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.OTPVerificationScreen.route,
                content = {
                    OTPVerificationScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.RegisterScreen.route,
                content = {
                    RegisterScreen(
                        navHostController = navHostController,
                        mainViewModel = mainViewModel
                    )
                }
            )

            composable2 (
                route = Routes.DashboardScreen.route,
                content = {
                    DashboardScreen(
                        navHostController = navHostController,
                        mainViewModel = mainViewModel
                    )
                }
            )






        }


    }

}