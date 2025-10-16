package com.headway.bablicabdriver.model.registration.profile

import android.net.Uri

data class UpdateProfileResponse(
    val data: ProfileData,
    val message: String,
    val status: Boolean
)
data class UpdateProfileRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val profile_photo: Uri?,
    val dob: String,
)

data class ProfileData(
    val dob: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val profile_photo: Any,
    val user_id: String
)
