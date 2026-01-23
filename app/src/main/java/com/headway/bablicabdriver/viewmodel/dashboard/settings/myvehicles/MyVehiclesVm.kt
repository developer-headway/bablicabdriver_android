package com.headway.bablicabdriver.viewmodel.dashboard.settings.myvehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.MyVehiclesData
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.MyVehiclesResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyVehiclesVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _myVehicleList  = MutableStateFlow<List<MyVehiclesData?>?>(emptyList())
    val myVehicleList: MutableStateFlow<List<MyVehiclesData?>?> = _myVehicleList

    fun callMyVehiclesApi(
        token: String,
        errorStates: ErrorsData,
        onSuccess: (MyVehiclesResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callMyVehiclesApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    _myVehicleList.value = it?.data
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