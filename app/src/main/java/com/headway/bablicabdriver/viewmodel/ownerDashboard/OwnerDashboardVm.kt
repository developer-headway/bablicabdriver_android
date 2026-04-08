package com.headway.bablicabdriver.viewmodel.ownerDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.ownerDashboard.OwnerDashboardData
import com.headway.bablicabdriver.model.ownerDashboard.OwnerDashboardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class OwnerDashboardVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set


    private val _ownerDashboardData = MutableStateFlow<OwnerDashboardData?>(null)
    val ownerDashboardData : MutableStateFlow<OwnerDashboardData?> = _ownerDashboardData


    fun callOwnerDashboardApi(
        token: String,
        errorStates: ErrorsData,
        onSuccess: (OwnerDashboardResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callOwnerDashboardApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    _ownerDashboardData.value = it?.data
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