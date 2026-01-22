package com.headway.bablicabdriver.res.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts


@Composable
fun FinishRideDialog(
    onDismiss: () -> Unit,
    onCancelRide: () -> Unit,
    onGoBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Go Back (filled button)
                FilledButtonGradient(
                    text = "Go Back",
                    isBorder = false,
                    backgroundColor = MyColors.clr_00BCF1_100,
                    textColor = MyColors.clr_white_100,
                    buttonHeight = 48.dp,
                    radius = 10.dp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp),
                    onClick = { onGoBack() }
                )

                // Cancel Ride (outlined button)
                FilledButtonGradient(
                    text = "Finish Ride",
                    isBorder = true,
                    borderColor = MyColors.clr_00BCF1_100,
                    textColor = MyColors.clr_00BCF1_100,
                    buttonHeight = 48.dp,
                    radius = 10.dp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.dp),
                    onClick = { onCancelRide() }
                )


            }
        },
        title = {
            TextView(
                text = "Are you sure you want to finish ride?",
                fontSize = 18.sp,
                fontFamily = MyFonts.fontSemiBold,
                textColor = MyColors.clr_black_100
            )
        },
        text = {
            TextView(
                text = "",
                fontSize = 15.sp,
                fontFamily = MyFonts.fontRegular,
                textColor = MyColors.clr_707070_100,
                textAlign = TextAlign.Start
            )
        },
        shape = RoundedCornerShape(14.dp),
        containerColor = MyColors.clr_white_100
    )
}


