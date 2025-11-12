package com.headway.bablicabdriver.screen.dashboard.home.rideDialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.buttons.FilledButton
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textfields.OtpInputField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun StartRideDialog(
    modifier: Modifier = Modifier,
    onStartRideClick: (String?) -> Unit = {}
) {
    val context = LocalContext.current
    val otp = rememberSaveable {
        mutableStateOf("")
    }
    var otpError by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .neu(
                shadowElevation = 2.dp,
                shape = Flat(RoundedCorner(10.dp)),
                lightShadowColor = MyColors.clr_7E7E7E_13,
                darkShadowColor = MyColors.clr_7E7E7E_13
            )
            .background(
                color = MyColors.clr_white_100,
                shape = RoundedCornerShape(10.dp)
            )
//            .align(Alignment.BottomCenter)
    ) {
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        TextView(
            text = stringResource(R.string.start_ride),
            fontFamily = MyFonts.fontBold,
            fontSize = 16.sp,
            textColor = MyColors.clr_black_100,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Spacer(
            modifier = Modifier
                .height(12.dp)
        )

        TextView(
            text = stringResource(R.string.enter_otp),
            fontFamily = MyFonts.fontBold,
            fontSize = 16.sp,
            textColor = MyColors.clr_black_100,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Spacer(
            modifier = Modifier
                .height(12.dp)
        )

        TextView(
            text = stringResource(R.string.please_enter_the_otp),
            fontFamily = MyFonts.fontRegular,
            fontSize = 12.sp,
            textColor = MyColors.clr_black_100,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        OtpInputField(
            otp,
            isTyping = {
                otpError = false
            }
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        FilledButtonGradient (
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .align(Alignment.CenterHorizontally),
            backgroundColor = MyColors.clr_08875D_100,
            text = stringResource(R.string.start_ride),
            onClick = {
                onStartRideClick(otp.value)
            }
        )
        Spacer(
            modifier = Modifier
                .height(30.dp)
        )
    }
}

