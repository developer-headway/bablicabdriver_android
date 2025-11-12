package com.headway.bablicabdriver.model.dashboard.home

data class RidePaymentRequest(
    val ride_id: String,
    val transaction_id: String,
    val is_cash: Boolean,
    val total_amount: String,
)

data class RidePaymentResponse(
    val data: Any,
    val message: String,
    val status: Boolean
)


