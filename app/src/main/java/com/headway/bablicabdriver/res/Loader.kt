package com.headway.bablicabdriver.res

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.ui.theme.MyColors
import kotlinx.coroutines.delay

@Composable
fun Loader(hideKeyboard: Boolean = true) {
    val focusManager = LocalFocusManager.current
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            // here `code` is the file name of lottie file
            // use it accordingly
            .RawRes(R.raw.loader)
    )
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color(0xff969695).hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "**"
            )
        )
    )
    if (hideKeyboard) {
        LaunchedEffect(key1 = true, block = {
            delay(200)
            focusManager.clearFocus(true)
        })
    }
    Box(
        modifier = Modifier
        .fillMaxSize()
            .background(MyColors.clr_black_20),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition, iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(100.dp),
                speed = 1f
            )
        }
    }
}