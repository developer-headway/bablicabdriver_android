package com.headway.bablicabdriver.viewmodel.registration

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.registration.UploadDocumentRequest
import com.headway.bablicabdriver.model.registration.UploadDocumentResponse
import com.headway.bablicabdriver.utils.createImgBodyPart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UploadDocumentVm : ViewModel() {
    var _isLoading = MutableStateFlow(false)
        private set

    fun callUploadDocumentApi(
        application: Application?,
        type: String = "RC",
        token: String,
        request: UploadDocumentRequest,
        errorStates: ErrorsData,
        onSuccess: (UploadDocumentResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    val vehicleNumber =
                        request.vehicle_number.toRequestBody("text/plain".toMediaTypeOrNull())
                    val dlNumber =
                        request.dl_number.toRequestBody("text/plain".toMediaTypeOrNull())
                    val aadharNumber =
                        request.aadhar_number.toRequestBody("text/plain".toMediaTypeOrNull())
                    val panNumber =
                        request.pan_number.toRequestBody("text/plain".toMediaTypeOrNull())

                    when (type) {
                        "RC" -> {
                            RetrofitApiClient.apiInterface.callUploadDocumentApi(
                                token = "Bearer $token",
                                vehicle_number = vehicleNumber,
                                RCbook_front_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.RCbook_front_photo,
                                    label = "RCbook_front_photo"
                                ),
                                RCbook_back_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.RCbook_back_photo,
                                    label = "RCbook_back_photo"
                                ),
                            )
                        }

                        "aadhar" -> {
                            RetrofitApiClient.apiInterface.callUploadAadharApi(
                                token = "Bearer $token",
                                aadhar_number = aadharNumber,
                                aadhar_front_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.aadhar_front_photo,
                                    label = "aadhar_front_photo"
                                ),
                                aadhar_back_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.aadhar_back_photo,
                                    label = "aadhar_back_photo"
                                ),
                            )
                        }

                        "DL" -> {
                            RetrofitApiClient.apiInterface.callUploadLicenceApi(
                                token = "Bearer $token",
                                dl_number = dlNumber,
                                licence_front_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.licence_front_photo,
                                    label = "licence_front_photo"
                                ),
                                licence_back_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.licence_back_photo,
                                    label = "licence_back_photo"
                                )
                            )
                        }

                        "pan" -> {
                            RetrofitApiClient.apiInterface.callUploadPanApi(
                                token = "Bearer $token",
                                pan_number = panNumber,
                                pan_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.pan_photo,
                                    label = "pan_photo"
                                )
                            )
                        }

                        "police_verification" -> {
                            RetrofitApiClient.apiInterface.callUploadPoliceVerificationApi(
                                token = "Bearer $token",
                                police_verification_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.police_verification_photo,
                                    label = "police_verification_photo"
                                )
                            )
                        }

                        else -> {
                            RetrofitApiClient.apiInterface.callUploadDocumentApi(
                                token = "Bearer $token",
                                vehicle_number = vehicleNumber,
                                RCbook_front_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.RCbook_front_photo,
                                    label = "RCbook_front_photo"
                                ),
                                RCbook_back_photo = createImgBodyPart(
                                    context = application,
                                    uri = request.RCbook_back_photo,
                                    label = "RCbook_back_photo"
                                )
                            )
                        }
                    }


                },
                errorStates = errorStates,
                onSuccess = {
                    onSuccess(it)
                },
                isLoading = _isLoading,
                onError = {
                    onError(it)
                },
                onErrorResponse = { errorBody ->
                    val errorMessage = errorBody?.message
                    onError(errorMessage)
                }
            )
        }
    }

}