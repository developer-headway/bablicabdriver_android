package com.headway.bablicabdriver.viewmodel.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.registration.DocumentData
import com.headway.bablicabdriver.model.registration.RegistrationDetailsData
import com.headway.bablicabdriver.model.registration.RegistrationDetailsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegistrationDetailsVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set
    private val _registrationDetailsData = MutableStateFlow<RegistrationDetailsData?>(null)
    val registrationDetailsData : MutableStateFlow<RegistrationDetailsData?> = _registrationDetailsData


    fun callRegistrationDetailsApi(
        token: String,
        errorStates: ErrorsData,
        onSuccess: (RegistrationDetailsResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callRegistrationDetailsApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    if (it?.status==true) {
                        _registrationDetailsData.value = it.data
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