package com.headway.bablicabdriver.viewmodel.dashboard.settings.myvehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.MyVehiclesData
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.VehicleDetailRequest
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.VehicleDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VehicleDetailsVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _myVehiclesData  = MutableStateFlow<MyVehiclesData?>(null)
    val myVehiclesData: MutableStateFlow<MyVehiclesData?> = _myVehiclesData

    fun callVehicleDetailsApi(
        token: String,
        request: VehicleDetailRequest,
        errorStates: ErrorsData,
        onSuccess: (VehicleDetailResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callVehicleDetailsApi(
                        token = "Bearer $token",
                        request = request
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    _myVehiclesData.value = it?.data
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