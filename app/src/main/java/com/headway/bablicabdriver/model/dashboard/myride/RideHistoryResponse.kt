package com.headway.bablicabdriver.model.dashboard.myride

import com.headway.bablicabdriver.model.commondataclass.Pagination
import java.io.Serializable

data class RideHistoryResponse(
    val data: RideHistoryData,
    val message: String,
    val status: Boolean
)

data class RideHistoryData(
    val pagination: Pagination,
    val rides: List<RideData>
)
data class RideData(
    val booking_type: String,
    val comment: String,
    val customer_name: String,
    val customer_phone_number: String,
    val customer_profile_image: String,
    val destination_Latitude: Double,
    val destination_Longitude: Double,
    val destination_address: String,
    val pickup_Latitude: Double,
    val pickup_Longitude: Double,
    val pickup_address: String,
    val ride_date: String,
    val ride_status: String,
    val ride_time: String,
    val ride_type: String,
    val star_rating: Float,
    val total_price: Double,
    val trip_distance: Double
): Serializable

data class RideHistoryRequest(
    val page: String = "1",
)
