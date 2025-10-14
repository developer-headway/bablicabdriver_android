package com.headway.bablicabdriver.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {
    var removePadding = MutableStateFlow(false)
        private set

    var showSnackBar = MutableStateFlow(false)
        private set

    var snackBarText = MutableStateFlow("")
        private set


    fun updateSnackBar(show : Boolean){
        showSnackBar.value = show
    }

    fun updateSnackText(string : String){
        snackBarText.value = string
    }

}