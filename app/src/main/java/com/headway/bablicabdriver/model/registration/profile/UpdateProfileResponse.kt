package com.headway.bablicabdriver.model.registration.profile

import android.net.Uri
import com.headway.bablicabdriver.model.commondataclass.Pagination

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
    val RCbook_back_photo: String,
    val RCbook_front_photo: String,
    val aadhar_back_photo: String,
    val aadhar_front_photo: String,
    val aadhar_number: String,
    val dl_number: String,
    val dob: String,
    val email: String,
    val first_name: String,
    val isNewUser: Boolean,
    val is_approved: Boolean,
    val last_name: String,
    val licence_back_photo: String,
    val licence_front_photo: String,
    val mobile: String,
    val pan_number: String,
    val pan_photo: String,
    val phone_number: String,
    val police_verification_photo: String,
    val profile_photo: String,
    val user_id: String,
    val user_type: String,
    val vehicle_number: String
)





