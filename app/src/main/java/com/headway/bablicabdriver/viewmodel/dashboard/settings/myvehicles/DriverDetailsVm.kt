package com.headway.bablicabdriver.viewmodel.dashboard.settings.myvehicles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverDetailsData
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverDetailsRequest
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverDetailsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DriverDetailsVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _driverDetailsData  = MutableStateFlow<DriverDetailsData?>(null)
    val driverDetailsData: MutableStateFlow<DriverDetailsData?> = _driverDetailsData

    fun callDriverDetailsApi(
        token: String,
        request: DriverDetailsRequest,
        errorStates: ErrorsData,
        onSuccess: (DriverDetailsResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callDriverDetailsApi(
                        token = "Bearer $token",
                        request = request
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    if (it?.status==true) {
                        _driverDetailsData.value = it.data
                    }
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

    override fun onCleared() {
        Log.d("VehicleVM", "CLEARED")
    }

}