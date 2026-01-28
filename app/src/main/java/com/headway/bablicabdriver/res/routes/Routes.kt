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
    data object WaitingScreen : Routes(route = "WaitingScreen")
    data object MyRideDetailsScreen : Routes(route = "MyRideDetailsScreen")
    data object DocumentInfoScreen : Routes(route = "DocumentInfoScreen")
    data object DocumentDetailsScreen : Routes(route = "DocumentDetailsScreen")
    data object BankDetailsScreen : Routes(route = "BankDetailsScreen")
    data object PaymentScreen : Routes(route = "PaymentScreen")
    data object PaymentSuccessScreen : Routes(route = "PaymentSuccessScreen")
    data object NotificationListScreen : Routes(route = "NotificationListScreen")
    data object SetRouteScreen : Routes(route = "SetRouteScreen")
    data object MyVehicleScreen : Routes(route = "MyVehicleScreen")
    data object DriverListScreen : Routes(route = "DriverListScreen")
    data object SettingsScreen : Routes(route = "SettingsScreen")
    data object TransactionsScreen : Routes(route = "TransactionsScreen")

    data object VehicleDetailsScreen : Routes(route = "VehicleDetailsScreen/{vehicleId}") {
        fun createRoute(vehicleId: String) = "VehicleDetailsScreen/$vehicleId"
    }

    data object DriverDetailsScreen : Routes(route = "DriverDetailsScreen/{driverId}") {
        fun createRoute(driverId: String) = "DriverDetailsScreen/$driverId"
    }


}