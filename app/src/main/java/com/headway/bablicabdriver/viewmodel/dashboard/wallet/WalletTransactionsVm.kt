package com.headway.bablicabdriver.viewmodel.dashboard.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.commondataclass.Pagination
import com.headway.bablicabdriver.model.dashboard.wallet.Transaction
import com.headway.bablicabdriver.model.dashboard.wallet.WalletTransactionsRequest
import com.headway.bablicabdriver.model.dashboard.wallet.WalletTransactionsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WalletTransactionsVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _transactionList  = MutableStateFlow<List<Transaction?>?>(emptyList())
    val transactionList: MutableStateFlow<List<Transaction?>?> = _transactionList

    private var _pagination  = MutableStateFlow<Pagination?>(null)
    val pagination: MutableStateFlow<Pagination?> = _pagination


    fun callWalletTransactionsApi(
        token: String,
        request: WalletTransactionsRequest,
        errorStates: ErrorsData,
        onSuccess: (WalletTransactionsResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callWalletTransactionsApi(
                        token = "Bearer $token",
                        request = request
                    )
                },
                errorStates = errorStates,
                onSuccess = {

                    if (it?.status == true) {
                        _pagination.value = it.data.pagination

                        if (it.data.pagination?.current_page == 1) {
                            _transactionList.value = it.data.transactions
                        } else {
                            val currentList = _transactionList.value.orEmpty().toMutableList()
                            currentList.addAll(it.data.transactions?: emptyList())
                            _transactionList.value = currentList
                        }

                    } else {
                        _transactionList.value = emptyList()
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

}