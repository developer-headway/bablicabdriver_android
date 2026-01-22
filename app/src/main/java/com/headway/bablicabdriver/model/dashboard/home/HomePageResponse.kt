package com.headway.bablicabdriver.model.dashboard.home

import java.io.Serializable

data class HomePageResponse(
    val data: HomePageData,
    val message: String,
    val status: Boolean
)

data class HomePageData(
    val Current_ride: CurrentRide?,
//    val Current_shuttle_ride_passengers: List<CurrentShuttleRidePassenger?>?,
    val Current_shuttle_ride_passengers: List<CurrentShuttleRidePassenger>,
    val Ride_requests: List<RideRequests?>?,
    val current_ride_type: CurrentRideType?,
    val is_online: Boolean,
    val razorpay_live_key: String,
    val razorpay_live_secret: String,
    val razorpay_test_key: String,
    val razorpay_test_secret: String,
    val vehicle_ideal_number: String,
)

data class CurrentRide(
    val ride_id: String,
    val booking_type: String,
    val ride_type: String,
    val pickup_address: String,
    val pickup_Latitude: Double,
    val pickup_Longitude: Double,
    val destination_address: String,
    val destination_Latitude: Double,
    val destination_Longitude: Double,
    val total_price: Double,
    val trip_distance: Double,
    val ride_status: String,
    val customer_name: String?,
    val customer_profile_image: String?,
    val customer_phone_number: String
)

data class RideRequests(
    val ride_id: String? = null,
    val booking_type: String? = null,
    val ride_type: String? = null,
    val pickup_address: String? = null,
    val pickup_Latitude: String? = null,
    val pickup_Longitude: String? = null,
    val destination_address: String? = null,
    val destination_Latitude: String? = null,
    val destination_Longitude: String? = null,
    val trip_distance: String? = null,
    val ride_status: String? = null,
    val customer_name: String? = null,
    val customer_profile_image: String? = null,
    val customer_phone_number: String? = null,
    val total_price: String? = null,
    val type: String? = null,

    // 🕒 Local-only properties (not part of API)
    @Transient var totalTime: Float = 10f,
    @Transient var remainingTime: Float = 10f,
    @Transient var timer: java.util.Timer? = null
): Serializable


data class CurrentShuttleRidePassenger(
    val drop_address: String,
    val passenger_count: Int,
    val pickup_address: String,
    val ride_amount: Double,
    val ride_id: String,
    val status: String,
    val trip_code: String,
    val user_id: String
)

data class CurrentRideType(
    val ride_type: String,
    val shuttle_route_end_selected_address: String,
    val shuttle_route_start_selected_address: String
)




//data class AAAAAAAAAAAAAAAAAA(
//    val `data`: Data,
//    val message: String,
//    val status: Boolean
//)
//
//data class Data(
//    val Current_ride: CurrentRide,
//    val Current_shuttle_ride_passengers: List<CurrentShuttleRidePassenger>,
//    val Ride_requests: List<Any>,
//    val current_ride_type: CurrentRideType,
//    val is_online: Boolean,
//    val razorpay_live_key: String,
//    val razorpay_live_secret: String,
//    val razorpay_test_key: String,
//    val razorpay_test_secret: String,
//    val vehicle_ideal_number: String
//)
//
//class CurrentRide
//
//data class CurrentShuttleRidePassenger(
//    val drop_address: String,
//    val passenger_count: Int,
//    val pickup_address: String,
//    val ride_amount: Double,
//    val ride_id: String,
//    val status: String,
//    val trip_code: String,
//    val user_id: String
//)
//
//data class CurrentRideType(
//    val ride_type: String,
//    val shuttle_route_end_selected_address: String,
//    val shuttle_route_start_selected_address: String
//)



