package com.headway.bablicabdriver.model.dashboard.settings.bankDetails

data class UpdateBankDetailsRequest(
    val bank_name: String,
    val account_holder_name: String,
    val account_no: String,
    val ifsc_code: String,
    val upi_id: String,
)

data class BankDetailsResponse(
    val data: BankDetailsData,
    val message: String,
    val status: Boolean
)

data class BankDetailsData(
    val account_holder_name: String,
    val account_no: String,
    val bank_name: String,
    val ifsc_code: String,
    val upi_id: String
)

