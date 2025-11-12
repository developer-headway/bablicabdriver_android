package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.CompleteRideRequest
import com.headway.bablicabdriver.model.dashboard.home.CompleteRideResponse
import com.headway.bablicabdriver.model.dashboard.home.RidePaymentRequest
import com.headway.bablicabdriver.model.dashboard.home.RidePaymentResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RidePaymentVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callRidePaymentApi(
        token: String,
        request: RidePaymentRequest,
        errorStates: ErrorsData,
        onSuccess: (RidePaymentResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callRidePaymentApi(
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