package com.headway.bablicabdriver.model.dashboard.wallet

data class WithdrawMoneyResponse(
    val message: String,
    val status: Boolean
)
data class WithdrawMoneyRequest(
    val amount: String,
)