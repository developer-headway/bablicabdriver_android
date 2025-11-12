package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.AcceptRideRequest
import com.headway.bablicabdriver.model.dashboard.home.AcceptRideResponse
import com.headway.bablicabdriver.model.dashboard.home.SetOnlineStatusRequest
import com.headway.bablicabdriver.model.dashboard.home.SetOnlineStatusResponse
import com.headway.bablicabdriver.model.dashboard.home.UpdateDriverLocationRequest
import com.headway.bablicabdriver.model.dashboard.home.UpdateDriverLocationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AcceptRideVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callAcceptRideApi(
        token: String,
        request: AcceptRideRequest,
        errorStates: ErrorsData,
        onSuccess: (AcceptRideResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callAcceptRideApi(
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