package com.headway.bablicabdriver.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.commondataclass.CommonResponse
import com.headway.bablicabdriver.model.commondataclass.DeviceUIdRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LogoutVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callLogoutApi(
        token: String,
        errorStates: ErrorsData,
        request: DeviceUIdRequest,
        onSuccess: (CommonResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {
        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callLogoutApi(
                        token = "Bearer $token",
                        request = request
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