package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.ShuttleRouteData
import com.headway.bablicabdriver.model.dashboard.home.ShuttleRouteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ShuttleRouteVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _shuttleRouteData  = MutableStateFlow<List<ShuttleRouteData?>?>(emptyList())
    val shuttleRouteList: MutableStateFlow<List<ShuttleRouteData?>?> = _shuttleRouteData

    fun callShuttleRouteApi(
        token: String,
        errorStates: ErrorsData,
        onSuccess: (ShuttleRouteResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callShuttleRouteApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    _shuttleRouteData.value = it?.data
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