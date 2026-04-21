package com.headway.bablicabdriver.viewmodel.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.settings.refreshment.RefreshmentItemData
import com.headway.bablicabdriver.model.dashboard.settings.refreshment.RefreshmentItemsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RefreshmentItemsVm : ViewModel() {

    var _isLoading = MutableStateFlow(false)
        private set

    private val _itemsData = MutableStateFlow<RefreshmentItemsData?>(null)
    val itemsData: MutableStateFlow<RefreshmentItemsData?> = _itemsData

    fun callRefreshmentItemsApi(
        token: String,
        errorStates: ErrorsData,
        onError: (String?) -> Unit = {}
    ) {
        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callRefreshmentItemsApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = { response ->
                    if (response?.status == true) {
                        _itemsData.value = response.data
                    } else {
                        onError(response?.message)
                    }
                },
                isLoading = _isLoading,
                onError = { onError(it) },
                onErrorResponse = { onError(it?.message) }
            )
        }
    }
}
