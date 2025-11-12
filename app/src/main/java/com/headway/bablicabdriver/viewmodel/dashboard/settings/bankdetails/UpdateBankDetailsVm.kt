package com.headway.bablicabdriver.viewmodel.dashboard.settings.bankdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.settings.bankDetails.BankDetailsResponse
import com.headway.bablicabdriver.model.dashboard.settings.bankDetails.UpdateBankDetailsRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UpdateBankDetailsVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    fun callUpdateBankDetailsApi(
        token: String,
        request: UpdateBankDetailsRequest,
        errorStates: ErrorsData,
        onSuccess: (BankDetailsResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callUpdateBankDetailsApi(
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