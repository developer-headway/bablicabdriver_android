package com.headway.bablicabdriver.model.dashboard.home

data class SetShuttleRouteRequest(
    val user_id: String,
    val ride_type: String,
    val route_id: String
)

data class SetShuttleRouteResponse(
    val data: SetShuttleRouteData,
    val message: String,
    val status: Boolean
)

data class SetShuttleRouteData(
    val route_end_point: String,
    val route_start_point: String,
    val shuttle_code: String,
    val shuttle_id: String
)