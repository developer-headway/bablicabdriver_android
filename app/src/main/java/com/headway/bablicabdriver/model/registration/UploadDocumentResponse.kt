package com.headway.bablicabdriver.model.registration

import android.net.Uri


data class UploadDocumentRequest(
    val RCbook_front_photo: Uri? = null,
    val RCbook_back_photo: Uri? = null,
    val vehicle_number: String = "",

    val licence_front_photo: Uri? = null,
    val licence_back_photo: Uri? = null,
    val dl_number: String = "",

    val aadhar_front_photo: Uri? = null,
    val aadhar_back_photo: Uri? = null,
    val aadhar_number: String = "",

    val pan_photo: Uri? = null,
    val pan_number: String = "",
    val police_verification_photo: Uri? = null,
)

data class UploadDocumentResponse(
    val data: UploadDocumentData,
    val message: String,
    val status: Boolean
)

data class UploadDocumentData(
    val RCBook_completed_status: String,
    val RCbook_back_photo: String,
    val RCbook_front_photo: String,
    val aadhar_back_photo: String,
    val aadhar_completed_status: String,
    val aadhar_front_photo: String,
    val aadhar_number: String,
    val dl_number: String,
    val licence_back_photo: String,
    val licence_completed_status: String,
    val licence_front_photo: String,
    val pan_completed_status: String,
    val pan_number: String,
    val pan_photo: String,
    val police_verification_photo: String,
    val police_verification_status: String,
    val vehicle_number: String
)