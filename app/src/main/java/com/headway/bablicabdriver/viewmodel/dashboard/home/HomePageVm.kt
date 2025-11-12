package com.headway.bablicabdriver.viewmodel.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.dashboard.home.CurrentRide
import com.headway.bablicabdriver.model.dashboard.home.HomePageResponse
import com.headway.bablicabdriver.model.dashboard.home.RideRequests
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class HomePageVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private val _currentRide = MutableStateFlow<CurrentRide?>(null)
    val currentRide : MutableStateFlow<CurrentRide?> = _currentRide


    private val _rideRequestList = MutableStateFlow<List<RideRequests?>?>(emptyList())
    val rideRequestList : MutableStateFlow<List<RideRequests?>?> = _rideRequestList
    fun updateRideRequestList(data: RideRequests) {
        _rideRequestList.value = _rideRequestList.value?.plus(data)
    }
    fun removeRideRequestList(data: RideRequests?) {
        _rideRequestList.value = _rideRequestList.value?.minus(data)
    }
    fun emptyRideRequestList() {
        _rideRequestList.value = emptyList()
    }

    fun callHomePageApi(
        token: String,
        errorStates: ErrorsData,
        onSuccess: (HomePageResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callHomePageApi(
                        token = "Bearer $token"
                    )
                },
                errorStates = errorStates,
                onSuccess = {
                    if (it?.status==true) {
                        _currentRide.value = it.data.Current_ride
                        rideRequestList.value = it.data.Ride_requests
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