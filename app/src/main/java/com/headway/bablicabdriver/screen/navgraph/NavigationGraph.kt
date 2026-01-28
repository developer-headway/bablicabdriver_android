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
import com.headway.bablicabdriver.screen.dashboard.home.SetRouteScreen
import com.headway.bablicabdriver.screen.dashboard.home.completeRide.PaymentScreen
import com.headway.bablicabdriver.screen.dashboard.home.completeRide.PaymentSuccessScreen
import com.headway.bablicabdriver.screen.dashboard.myride.ridedetails.MyRideDetailsScreen
import com.headway.bablicabdriver.screen.dashboard.notification.NotificationListScreen
import com.headway.bablicabdriver.screen.dashboard.settings.SettingsScreen
import com.headway.bablicabdriver.screen.dashboard.settings.bankdetails.BankDetailsScreen
import com.headway.bablicabdriver.screen.dashboard.settings.documentinfo.DocumentDetailsScreen
import com.headway.bablicabdriver.screen.dashboard.settings.documentinfo.DocumentInfoScreen
import com.headway.bablicabdriver.screen.dashboard.settings.drivers.DriverListScreen
import com.headway.bablicabdriver.screen.dashboard.settings.myvehicle.DriverDetailsScreen
import com.headway.bablicabdriver.screen.dashboard.settings.myvehicle.MyVehicleScreen
import com.headway.bablicabdriver.screen.dashboard.settings.myvehicle.VehicleDetailsScreen
import com.headway.bablicabdriver.screen.dashboard.wallet.transactions.TransactionsScreen
import com.headway.bablicabdriver.screen.intro.IntroScreen
import com.headway.bablicabdriver.screen.launch.LaunchScreen
import com.headway.bablicabdriver.screen.login.LoginScreen
import com.headway.bablicabdriver.screen.login.OTPVerificationScreen
import com.headway.bablicabdriver.screen.login.WebScreen
import com.headway.bablicabdriver.screen.registration.RegistrationScreen
import com.headway.bablicabdriver.screen.registration.WaitingScreen
import com.headway.bablicabdriver.screen.registration.profile.ProfileScreen
import com.headway.bablicabdriver.screen.registration.rcBookDetails.RCBookDetailsScreen
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

    //map initialize
//    MapsInitializer.initialize(context)

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
                route = Routes.RegistrationScreen.route,
                content = {
                    RegistrationScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.ProfileScreen.route,
                content = {
                    ProfileScreen(
                        navHostController = navHostController,
                        mainViewModel = mainViewModel
                    )
                }
            )

            composable2 (
                route = Routes.WaitingScreen.route,
                content = {
                    WaitingScreen(
                        navHostController = navHostController
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



            composable2 (
                route = Routes.RCBookDetailsScreen.route,
                content = {
                    RCBookDetailsScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.MyRideDetailsScreen.route,
                content = {
                    MyRideDetailsScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.DocumentInfoScreen.route,
                content = {
                    DocumentInfoScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.DocumentDetailsScreen.route,
                content = {
                    DocumentDetailsScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.BankDetailsScreen.route,
                content = {
                    BankDetailsScreen(
                        navHostController = navHostController,
                        mainViewModel = mainViewModel
                    )
                }
            )

            composable2 (
                route = Routes.PaymentScreen.route,
                content = {
                    PaymentScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.PaymentSuccessScreen.route,
                content = {
                    PaymentSuccessScreen(
                        navHostController = navHostController
                    )
                }
            )

            composable2 (
                route = Routes.NotificationListScreen.route,
                content = {
                    NotificationListScreen(
                        navHostController = navHostController,
                    )
                }
            )



            composable2 (
                route = Routes.SetRouteScreen.route,
                content = {
                    SetRouteScreen(
                        navHostController = navHostController,
                    )
                }
            )

            composable2 (
                route = Routes.MyVehicleScreen.route,
                content = {
                    MyVehicleScreen(
                        navHostController = navHostController,
                    )
                }
            )

            composable2 (
                route = Routes.VehicleDetailsScreen.route,
                content = { backStackEntry ->
                    val vehicleId = backStackEntry.arguments?.getString("vehicleId")
                    VehicleDetailsScreen(
                        navHostController = navHostController,
                        vehicleId = vehicleId
                    )
                }
            )

            composable2 (
                route = Routes.DriverDetailsScreen.route,
                content = { backStackEntry ->
                    val driverId = backStackEntry.arguments?.getString("driverId")
                    DriverDetailsScreen(
                        navHostController = navHostController,
                        driverId = driverId
                    )
                }
            )

            composable2 (
                route = Routes.DriverListScreen.route,
                content = { backStackEntry ->
                    DriverListScreen(
                        navHostController = navHostController
                    )
                }
            )


            composable2 (
                route = Routes.SettingsScreen.route,
                content = { backStackEntry ->
                    SettingsScreen(
                        navHostController,
                        mainViewModel
                    )
                }
            )

            composable2 (
                route = Routes.TransactionsScreen.route,
                content = {
                    TransactionsScreen(
                        navHostController = navHostController
                    )
                }
            )







        }

    }

}