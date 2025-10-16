package com.headway.bablicabdriver.model.registration

import android.net.Uri

data class RegistrationDetailsResponse(
    val data: RegistrationDetailsData,
    val message: String,
    val status: Boolean
)

data class DocumentData(
    val title: Int,
    val data: RegistrationDetailsData? = null
)


data class RegistrationDetailsData(
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
    val profile_completed_status: String
)


