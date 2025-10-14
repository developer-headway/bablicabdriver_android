package com.headway.bablicabdriver.api

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.jvm.java
import kotlin.let
import kotlin.toString


suspend inline fun <reified T> makeApiCall(
    crossinline apiCall: suspend () -> Response<T>,
    errorStates: ErrorsData,
    onSuccess: (T?) -> Unit,
    onErrorResponse: (T?) -> Unit,
    isLoading: MutableStateFlow<Boolean>,
    onError : (String) -> Unit = {}
) {
    try {
        isLoading.value = true
        val response = withContext(Dispatchers.IO) {
            apiCall()
        }


        if (response.isSuccessful) {
            Log.d("success", response.toString())
            when (response.code()) {
                200, 201, 204 -> {
                    onSuccess(response.body())
                    Log.d("success", response.body().toString())
                }
                else -> {
                    Log.d("else error", response.toString())
                    onError(response.message())
                }
            }
        } else {
            Log.d("HTTP error like 400", response.toString())
            // HTTP error like 400
            val errorBody = response.errorBody()
            val errorString = errorBody?.string()

            when (response.code()) {
                401 -> {
                    errorStates.showSessionError.value = true
                }
                500 -> {
                    errorStates.showInternalServerError.value = true
                }
                else -> {

                    try {
                        val res = Gson().fromJson(errorString, T::class.java)
                        onErrorResponse(res)
                    } catch (e: Exception) {
                        Log.d("else error", response.toString())
                        onError(response.message())
                    }
                }
            }
        }

    } catch (exception: SocketTimeoutException) {
        Log.d("@@@","exception:${exception.message}")

        exception.message?.let {
            onError(it)
        }
    } catch (e: Exception) {
        Log.d("error", e.toString())
        e.message?.let {
            onError(it)
        }
    } finally {
        isLoading.value = false
    }
}

data class ErrorsData(
    var showSessionError: MutableState<Boolean> = mutableStateOf(false),
    var showInternetError : MutableState<Boolean> = mutableStateOf(false),
    var showInternalServerError : MutableState<Boolean> = mutableStateOf(false),
    var bottomToastText : MutableState<String> = mutableStateOf(""),
    var showBottomToast : MutableState<Boolean> = mutableStateOf(false),
    var showUnderMaintenanceDialog : MutableState<Boolean> = mutableStateOf(false),
    var isLoading : MutableState<Boolean> = mutableStateOf(false),
    var showImageDialog : MutableState<Boolean> = mutableStateOf(false),
    var imageUrl : MutableState<String> = mutableStateOf(""),
)

enum class NetWorkFail{
    NoError,
    NetworkError
}