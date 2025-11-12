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

}