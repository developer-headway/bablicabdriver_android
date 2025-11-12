package com.headway.bablicabdriver.model.dashboard.wallet

import com.headway.bablicabdriver.model.commondataclass.Pagination

data class WalletTransactionsRequest(
    val page: String = "1",
)

data class WalletTransactionsResponse(
    val data: WalletTransactionsData,
    val message: String,
    val status: Boolean
)

data class WalletTransactionsData(
    val pagination: Pagination?,
    val total_wallet_balance: Int,
    val transactions: List<Transaction?>?
)



data class Transaction(
    val amount: Double,
    val date_time: String,
    val description: String,
    val id: String,
    val rejected_reason: String,
    val transaction_id: Any,
    val type: String
)

