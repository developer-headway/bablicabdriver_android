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
    val user_type: String,

    val RCbook_back_photo: String,
    val RCbook_front_photo: String,
    val aadhar_back_photo: String,
    val aadhar_front_photo: String,
    val aadhar_number: String,
    val dl_number: String,
    val licence_back_photo: String,
    val licence_front_photo: String,
    val pan_number: String,
    val pan_photo: String,
    val police_verification_photo: String,
    val vehicle_number: String,

    val RCBook_completed_status: String,
    val aadhar_completed_status: String,
    val is_Aadhar_completed: Int,
    val is_Pan_completed: Int,
    val is_RCbook_completed: Int,
    val is_licence_completed: Int,
    val is_police_verification_completed: Int,
    val is_profile_completed: Int,
    val licence_completed_status: String,
    val pan_completed_status: String,
    val police_verification_status: String,
    val profile_completed_status: String,

    val gender: String,
    val is_owner_driver: Boolean
)