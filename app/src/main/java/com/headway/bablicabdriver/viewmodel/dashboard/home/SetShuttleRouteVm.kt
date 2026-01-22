package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.SetShuttleRouteRequest
import com.headway.bablicabdriver.model.dashboard.home.SetShuttleRouteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SetShuttleRouteVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set


    fun callSetShuttleRouteApi(
        token: String,
        request: SetShuttleRouteRequest,
        errorStates: ErrorsData,
        onSuccess: (SetShuttleRouteResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callSetShuttleRouteApi(
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