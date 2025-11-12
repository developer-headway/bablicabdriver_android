package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.ArrivedPickupRequest
import com.headway.bablicabdriver.model.dashboard.home.ArrivedPickupResponse
import com.headway.bablicabdriver.model.dashboard.home.StartRideRequest
import com.headway.bablicabdriver.model.dashboard.home.StartRideResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartRideVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callStartRideApi(
        token: String,
        request: StartRideRequest,
        errorStates: ErrorsData,
        onSuccess: (StartRideResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callStartRideApi(
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