package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.CompleteRideRequest
import com.headway.bablicabdriver.model.dashboard.home.CompleteRideResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CompleteRideVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callCompleteRideApi(
        token: String,
        request: CompleteRideRequest,
        errorStates: ErrorsData,
        onSuccess: (CompleteRideResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callCompleteRideApi(
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