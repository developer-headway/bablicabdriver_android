package com.headway.bablicabdriver.model.ownerDashboard

data class OwnerDashboardResponse(
    val data: OwnerDashboardData,
    val message: String,
    val status: Boolean
)

data class OwnerDashboardData(
    val today_earning: Double,
    val total_drivers: Int,
    val total_earning: Double,
    val total_vehicles: Int
)





