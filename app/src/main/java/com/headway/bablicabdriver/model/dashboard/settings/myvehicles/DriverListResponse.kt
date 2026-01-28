package com.headway.bablicabdriver.model.dashboard.settings.myvehicles

data class DriverListResponse(
    val data: List<DriverDetailsData?>?,
    val message: String,
    val status: Boolean
)