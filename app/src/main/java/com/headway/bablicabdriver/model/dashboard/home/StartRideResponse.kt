package com.headway.bablicabdriver.model.dashboard.home

data class StartRideRequest(
    val ride_id: String,
    val otp: String,
)
data class StartRideResponse(
    val data: StartRideData,
    val message: String,
    val status: Boolean
)

data class StartRideData(
    val booking_type: String,
    val customer_name: String,
    val destination_Latitude: Double,
    val destination_Longitude: Double,
    val destination_address: String,
    val pickup_Latitude: Double,
    val pickup_Longitude: Double,
    val pickup_address: String,
    val ride_id: String,
    val ride_status: String,
    val ride_type: String,
    val total_price: Int,
    val trip_distance: Int
)
