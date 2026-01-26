package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.GOOGLE_API_KEY
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.ComputeRoutesRequest
import com.headway.bablicabdriver.model.dashboard.home.ComputeRoutesResponse
import com.headway.bablicabdriver.model.dashboard.home.RouteData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ComputeRouteVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private val _routeData = MutableStateFlow<RouteData?>(null)
    val routeData : MutableStateFlow<RouteData?> = _routeData

    fun callComputeRoutesApi(
        errorStates: ErrorsData,
        request: ComputeRoutesRequest,
        onSuccess: (ComputeRoutesResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {
        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.googleRouteApiInterface.callComputeRoutesApi(
                        token = GOOGLE_API_KEY,
                        request = request
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    if (!it?.routes.isNullOrEmpty()) {
                        _routeData.value = it.routes[0]
                    }
                    onSuccess(it)
                },
                isLoading = _isLoading,
                onError = {
                    onError(it)
                },
                onErrorResponse = {errorBody->
                    val errorMessage = errorBody?.error?.message
                    onError(errorMessage)
                }
            )
        }
    }

}