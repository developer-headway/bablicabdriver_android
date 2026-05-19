package com.headway.bablicabdriver.viewmodel.dashboard.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.wallet.AddMoneyRequest
import com.headway.bablicabdriver.model.dashboard.wallet.AddMoneyResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddMoneyVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set


    fun callAddMoneyApi(
        token: String,
        request: AddMoneyRequest,
        errorStates: ErrorsData,
        onSuccess: (AddMoneyResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callAddMoneyApi(
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