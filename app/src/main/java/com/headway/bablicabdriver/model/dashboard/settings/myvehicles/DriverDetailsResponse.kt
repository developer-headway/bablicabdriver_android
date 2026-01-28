package com.headway.bablicabdriver.model.dashboard.settings.myvehicles

data class DriverDetailsRequest(
    val driver_id: String
)



data class DriverDetailsResponse(
    val data: DriverDetailsData?,
    val message: String,
    val status: Boolean
)

data class DriverDetailsData(
    val aadhar_card_back_side_img_url: String,
    val aadhar_card_front_side_img_url: String,
    val aadhar_card_number: String,
    val dob: String,
    val driver_current_shift: String,
    val driver_id: String,
    val driver_image: String,
    val driver_name: String,
    val driving_licence_back_side_img_url: String,
    val driving_licence_front_side_img_url: String,
    val driving_licence_number: String,
    val email: String,
    val pan_card_front_side_img_url: String,
    val pan_card_number: String,
    val phone: String,
    val police_verification_doc_url: String,
    val total_earning: Int,
    val total_ride: Int,
    val wallet_amount: Double
)