package com.headway.bablicabdriver.model.login


data class VerifyOtpRequest(
    val device_type: String? = "Android",
    val country_code: String? = "91",
    val phone_number: String?,
    val otp: String?,
    val device_uid: String?,
    val device_token: String?,
)

data class VerifyOtpResponse(
    val data: VerifyOtpData,
    val message: String?,
    val status: Boolean
)

data class VerifyOtpData(
    val country_code: String,
    val email: String,
    val first_name: String,
    val isNewUser: Boolean,
    val is_approved: Boolean,
    val last_name: String,
    val phone_number: String,
    val profile_photo: String,
    val referral_code: String,
    val referred_by: String,
    val token: String,
    val user_id: String,
    val user_type: String
)

