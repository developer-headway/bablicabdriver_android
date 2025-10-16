package com.headway.bablicabdriver.viewmodel.registration.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.registration.profile.UpdateProfileRequest
import com.headway.bablicabdriver.model.registration.profile.UpdateProfileResponse
import com.headway.bablicabdriver.utils.createImgBodyPart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateProfileVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callUpdateProfileApi(
        application: Application?,
        token: String,
        request: UpdateProfileRequest,
        errorStates: ErrorsData,
        onSuccess: (UpdateProfileResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    val first_name = request.first_name.toRequestBody("text/plain".toMediaTypeOrNull())
                    val last_name = request.last_name.toRequestBody("text/plain".toMediaTypeOrNull())
                    val email = request.email.toRequestBody("text/plain".toMediaTypeOrNull())
                    val dob = request.dob.toRequestBody("text/plain".toMediaTypeOrNull())

                    RetrofitApiClient.apiInterface.callUpdateProfileApi(
                        token = "Bearer $token",
                        first_name = first_name,
                        last_name = last_name,
                        email = email,
                        dob = dob,
                        profile_photo = createImgBodyPart(
                            context = application,
                            uri = request.profile_photo,
                            label = "profile_photo"
                        ),
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    onSuccess(it)
                },
                isLoading = _isLoading,
                onError = {
                    onError(it)
                },
                onErrorResponse = {errorBody->
                    val errorMessage = errorBody?.message
                    onError(errorMessage)
                }
            )
        }
    }

}