package com.headway.bablicabdriver.model.dashboard.home

data class ShuttleRouteResponse(
    val data: List<ShuttleRouteData>,
    val message: String,
    val status: Boolean
)

data class ShuttleRouteData(
    val base_fare: Int,
    val city_id: String,
    val city_name: String,
    val route_end_point: String,
    val route_id: String,
    val route_name: String,
    val route_start_point: String
)