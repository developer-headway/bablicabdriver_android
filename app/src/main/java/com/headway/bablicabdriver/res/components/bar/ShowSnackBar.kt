package com.headway.bablicabdriver.res.components.bar

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.headway.bablicabdriver.ui.theme.MyColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShowSnackBar(
    message: String,
    showSb: Boolean,
    openSnackBar: (Boolean) -> Unit
) {
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val anchors = DraggableAnchors {
        0 at with(density){
            -screenWidth.dp.toPx()
        }
        1 at 0f
        2 at with(density){
            screenWidth.dp.toPx()
        }
    }

    val state = remember {
        AnchoredDraggableState(
            1,
            anchors = anchors,
            positionalThreshold = { totalDistance: Float -> totalDistance * 0.5f  },
            velocityThreshold = {
                with(density){
                    80.dp.toPx()
                }
            },
            snapAnimationSpec = spring(),
            decayAnimationSpec = exponentialDecay()
        )
    }

    LaunchedEffect(key1 = state.currentValue, block = {
        if (state.currentValue == 0 || state.currentValue == 2){
            snackState.currentSnackbarData?.dismiss()
            delay(300)
            state.snapTo(1)
        }
    })


    SnackbarHost(
        modifier = Modifier,
        hostState = snackState
    ){
        Snackbar(
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = state.requireOffset()
                }
                .anchoredDraggable(state, Orientation.Horizontal),
            snackbarData = it,
            containerColor = MyColors.clr_EAEBFC_100,
            contentColor = MyColors.clr_243369_100
        )
    }

    if (showSb){
        LaunchedEffect(Unit) {
            snackScope.launch {
                val snackBar = snackState.showSnackbar(
                    message,
                    duration = SnackbarDuration.Short
                )
                when (snackBar) {
                    SnackbarResult.ActionPerformed -> {
                        /* Handle snackbar action performed */
                    }
                    SnackbarResult.Dismissed -> {
                        /* Handle snackbar dismissed */
                    }
                }
            }
            openSnackBar(false)
        }

    }
}
