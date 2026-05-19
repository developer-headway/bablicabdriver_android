package com.headway.bablicabdriver.model.dashboard.wallet

data class AddMoneyRequest(
    val amount: String,
    val transaction_id: String
)

data class AddMoneyResponse(
    val message: String,
    val status: Boolean
)

