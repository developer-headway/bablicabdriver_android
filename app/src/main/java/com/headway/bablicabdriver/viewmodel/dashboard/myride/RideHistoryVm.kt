package com.headway.bablicabdriver.viewmodel.dashboard.myride

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.commondataclass.Pagination
import com.headway.bablicabdriver.model.dashboard.myride.RideData
import com.headway.bablicabdriver.model.dashboard.myride.RideHistoryRequest
import com.headway.bablicabdriver.model.dashboard.myride.RideHistoryResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RideHistoryVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _rideDataList  = MutableStateFlow<List<RideData?>?>(emptyList())
    val rideDataList: MutableStateFlow<List<RideData?>?> = _rideDataList

    private var _pagination  = MutableStateFlow<Pagination?>(null)
    val pagination: MutableStateFlow<Pagination?> = _pagination


    fun callRideHistoryApi(
        token: String,
        request: RideHistoryRequest,
        errorStates: ErrorsData,
        onSuccess: (RideHistoryResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callRideHistoryApi(
                        token = "Bearer $token",
                        request = request
                    )
                },
                errorStates = errorStates,
                onSuccess = {

                    if (it?.status == true) {
                        _pagination.value = it.data.pagination

                        if (it.data.pagination?.current_page == 1) {
                            _rideDataList.value = it.data.rides
                        } else {
                            val currentList = _rideDataList.value.orEmpty().toMutableList()
                            currentList.addAll(it.data.rides?: emptyList())
                            _rideDataList.value = currentList
                        }

                    } else {
                        _rideDataList.value = emptyList()
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