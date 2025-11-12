package com.headway.bablicabdriver.res.components.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun ToastExpandHorizontal(
    showBottomToast: MutableState<Boolean>, s: String,
    textColor: Color = MyColors.clr_243369_100
) {


    AnimatedVisibility(
        visible = showBottomToast.value,
        enter =
            expandHorizontally(expandFrom = Alignment.CenterHorizontally),
        exit = slideOutVertically(targetOffsetY = {
            it
        })
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyColors.clr_EAEBFC_100),
            contentAlignment = Alignment.Center
        ) {
            TextView(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 15.dp),
                textColor = textColor,
                text = s,
                fontSize = 10.sp,
                fontFamily = MyFonts.fontSemiBold,
            )
        }
    }
}