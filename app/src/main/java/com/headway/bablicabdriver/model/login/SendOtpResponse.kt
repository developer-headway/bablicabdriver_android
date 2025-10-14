package com.headway.bablicabdriver.model.login

data class SendOtpRequest(
    val device_type: String? = "android",
    val country_code: String? = "91",
    val user_type: String? = "user",
    val phone_number: String?
)

data class SendOtpResponse(
    val data: SendOtpData,
    val message: String?,
    val status: Boolean
)

data class SendOtpData(
    val otp: String
)
