package com.headway.bablicabdriver.model.dashboard.wallet

data class CreateOrderIdRequest(
    val amount: String,
    val currency: String,
    val receipt: String
)

data class CreateIdResponse(
    val amount: Int,
    val amount_due: Int,
    val amount_paid: Int,
    val attempts: Int,
    val created_at: Int,
    val currency: String,
    val entity: String,
    val id: String,
    val notes: List<Any>,
    val offer_id: Any,
    val receipt: String,
    val status: String
)