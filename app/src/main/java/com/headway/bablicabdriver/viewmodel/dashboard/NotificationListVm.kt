package com.headway.bablicabdriver.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.RetrofitApiClient
import com.headway.bablicabdriver.api.makeApiCall
import com.headway.bablicabdriver.model.commondataclass.Pagination
import com.headway.bablicabdriver.model.dashboard.notifocation.Notifications
import com.headway.bablicabdriver.model.dashboard.notifocation.NotificationsRequest
import com.headway.bablicabdriver.model.dashboard.notifocation.NotificationsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NotificationListVm : ViewModel(){
    var _isLoading  = MutableStateFlow(false)
        private set

    private var _notificationList  = MutableStateFlow<List<Notifications?>?>(emptyList())
    val notificationList: MutableStateFlow<List<Notifications?>?> = _notificationList

    private var _pagination  = MutableStateFlow<Pagination?>(null)
    val pagination: MutableStateFlow<Pagination?> = _pagination


    fun callNotificationsApi(
        token: String,
        request: NotificationsRequest,
        errorStates: ErrorsData,
        onSuccess: (NotificationsResponse?) -> Unit = {},
        onError: (String?) -> Unit = {},
    ) {

        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    RetrofitApiClient.apiInterface.callNotificationsApi(
                        token = "Bearer $token",
                        request = request
                    )
                },
                errorStates = errorStates,
                onSuccess = {

                    if (it?.status == true) {
                        _pagination.value = it.data.pagination

                        if (it.data.pagination?.current_page == 1) {
                            _notificationList.value = it.data.list
                        } else {
                            val currentList = _notificationList.value.orEmpty().toMutableList()
                            currentList.addAll(it.data.list?: emptyList())
                            _notificationList.value = currentList
                        }

                    } else {
                        _notificationList.value = emptyList()
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