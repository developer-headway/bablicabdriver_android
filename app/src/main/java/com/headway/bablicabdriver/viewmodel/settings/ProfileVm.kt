package com.headway.bablicabdriver.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.registration.profile.ProfileData
import com.headway.bablicabdriver.model.registration.profile.ProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set
    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData : MutableStateFlow<ProfileData?> = _profileData


    fun callProfileApi(
        token: String,
        errorStates: ErrorsData,
        onSuccess: (ProfileResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callProfileApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    if (it?.status==true) {
                        _profileData.value = it.data
                    }
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