package com.headway.bablicabdriver.viewmodel.dashboard.settings.myvehicles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverDetailsData
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverListResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DriverListVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _driverList  = MutableStateFlow<List<DriverDetailsData?>?>(null)
    val driverList: MutableStateFlow<List<DriverDetailsData?>?> = _driverList

    fun callDriverListApi(
        token: String,
        errorStates: ErrorsData,
        onSuccess: (DriverListResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callDriverListApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    if (it?.status==true) {
                        _driverList.value = it.data
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