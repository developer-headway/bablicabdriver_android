package com.headway.bablicabdriver.model.dashboard.home


data class UpdateDriverLocationRequest(
    val latitude: String,
    val longitude: String,
)

data class UpdateDriverLocationResponse(
    val data: UpdateDriverLocationData,
    val message: String,
    val status: Boolean
)

data class UpdateDriverLocationData(
    val current_location: CurrentLocation,
    val dob: String,
    val email: String,
    val first_name: String,
    val last_location_update: String,
    val last_name: String,
    val profile_photo: String,
    val user_id: String
)

data class CurrentLocation(
    val coordinates: List<Double>,
    val type: String
)


