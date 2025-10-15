package com.headway.bablicabdriver.res.routes

sealed class Routes(val route: String) {
    data object LaunchScreen : Routes(route = "LaunchScreen")
    data object IntroScreen : Routes(route = "IntroScreen")
    data object LoginScreen : Routes(route = "LoginScreen")
    data object WebPageScreen : Routes(route = "WebPageScreen")
    data object OTPVerificationScreen : Routes(route = "OTPVerificationScreen")

    data object DashboardScreen : Routes(route = "DashboardScreen")
    data object OpenRidesScreen : Routes(route = "OpenRidesScreen")
    data object RegistrationScreen : Routes(route = "RegistrationScreen")
    data object ProfileScreen : Routes(route = "ProfileScreen")
    data object RCBookDetailsScreen : Routes(route = "RCBookDetailsScreen")

    data object LocationsScreen : Routes(route = "LocationsScreen")
    data object LocationsMapScreen : Routes(route = "LocationsMapScreen")
    data object TransactionsScreen : Routes(route = "TransactionsScreen")
    data object AddMoneyScreen : Routes(route = "AddMoneyScreen")
    data object MyRidesScreen : Routes(route = "MyRidesScreen")
    data object MyRideDetailsScreen : Routes(route = "MyRideDetailsScreen")
    data object ReferEarnScreen : Routes(route = "ReferEarnScreen")
    data object SelectPaymentTypeScreen : Routes(route = "SelectPaymentTypeScreen")
    data object TripScreen : Routes(route = "TripScreen")
}