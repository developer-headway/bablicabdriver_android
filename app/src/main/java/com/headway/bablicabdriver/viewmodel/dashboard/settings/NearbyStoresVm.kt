package com.headway.bablicabdriver.viewmodel.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.settings.refreshment.StoreData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NearbyStoresVm : ViewModel() {

    var _isLoading = MutableStateFlow(false)
        private set

    private val _stores = MutableStateFlow<List<StoreData>?>(null)
    val stores: MutableStateFlow<List<StoreData>?> = _stores

    fun callNearbyStoresApi(
        token: String,
        lat: Double,
        lon: Double,
        radius: Int = 100,
        errorStates: ErrorsData,
        onError: (String?) -> Unit = {}
    ) {
        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callNearbyStoresApi(
                        token = "Bearer $token",
                        lat = lat,
                        lon = lon,
                        radius = radius
                    )
                },
                errorStates = errorStates,
                onSuccess = { response ->
                    if (response?.status == true) {
                        _stores.value = response.data?.stores
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
