package com.headway.bablicabdriver.res.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.res.components.toast.ToastExpandHorizontal

@Composable
fun CommonErrorDialogs(
    errorStates: ErrorsData,
    showToast: Boolean = true,
    onCreateTicket: () -> Unit = {},
    onOkUnderstood: () -> Unit = {},
    onNoInternetRetry: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (showToast) {
            ToastExpandHorizontal(
                showBottomToast = errorStates.showBottomToast,
                s = errorStates.bottomToastText.value
            )
        }
        InternetNotAvailable(visible = errorStates.showInternetError) {
            errorStates.showInternetError.value = false
            onNoInternetRetry()
        }

        AnimatedVisibility(
            visible = errorStates.showImageDialog.value,
            enter = slideInHorizontally(
                initialOffsetX = {it}
            ) + fadeIn(),
            exit = slideOutHorizontally(
                targetOffsetX = {
                    it
                }
            ) + fadeOut()
        ) {
//            CustomImageViewer(
//                image = errorStates.imageUrl.value,
//                onBack = {
//                    errorStates.showImageDialog.value = false
//                }
//            )
        }

//        SessionExpiredDialog(visible = errorStates.showSessionError) { }
//        InternalServerErrorDialog(visible = errorStates.showInternalServerError,
//            onCreateTicket = onCreateTicket::invoke,
//            onUnderstood = onOkUnderstood::invoke)
//        UnderMaintenanceDialog(errorStates.showUnderMaintenanceDialog)
    }
}