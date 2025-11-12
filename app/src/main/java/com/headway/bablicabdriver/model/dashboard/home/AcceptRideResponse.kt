package com.headway.bablicabdriver.model.dashboard.home

data class AcceptRideResponse(
    val message: String,
    val status: Boolean
)
data class AcceptRideRequest(
    val ride_id: String
)


