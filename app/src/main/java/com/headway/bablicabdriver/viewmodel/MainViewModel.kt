package com.headway.bablicabdriver.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {
    var removePadding = MutableStateFlow(false)
        private set

    var showSnackBar = MutableStateFlow(false)
        private set

    var snackBarText = MutableStateFlow("")
        private set

    var currentLocation = MutableStateFlow<Location?>(null)
        private set

    fun updateCurrentLocation(location: Location){
        currentLocation.value = location
    }

    fun updateSnackBar(show : Boolean){
        showSnackBar.value = show
    }

    fun updateSnackText(string : String){
        snackBarText.value = string
    }

    var paymentSuccess = MutableStateFlow<String?>(null)
        private set

    var paymentError = MutableStateFlow<String?>(null)
        private set
    fun onPaymentSuccess(message : String?){
        paymentSuccess.value = message
    }
    fun onPaymentError(message : String?){
        paymentError.value = message
    }

}