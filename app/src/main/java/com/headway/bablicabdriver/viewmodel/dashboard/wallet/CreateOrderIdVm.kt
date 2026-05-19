package com.headway.bablicabdriver.viewmodel.dashboard.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.wallet.CreateIdResponse
import com.headway.bablicabdriver.model.dashboard.wallet.CreateOrderIdRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateOrderIdVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set


    fun callCreateOrderIdApi(
        token: String,
        request: CreateOrderIdRequest,
        errorStates: ErrorsData,
        onSuccess: (CreateIdResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.razorpayInterface.callCreateOrderIdApi(
                        token = "Basic $token",
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
//                    val errorMessage = errorBody?.message
                    onError("error")
                }
            )
        }
    }

}