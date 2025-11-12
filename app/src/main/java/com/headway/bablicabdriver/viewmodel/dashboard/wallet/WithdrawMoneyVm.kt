package com.headway.bablicabdriver.viewmodel.dashboard.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.wallet.WithdrawMoneyRequest
import com.headway.bablicabdriver.model.dashboard.wallet.WithdrawMoneyResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WithdrawMoneyVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callWithdrawMoneyApi(
        token: String,
        request: WithdrawMoneyRequest,
        errorStates: ErrorsData,
        onSuccess: (WithdrawMoneyResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callWithdrawMoneyApi(
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