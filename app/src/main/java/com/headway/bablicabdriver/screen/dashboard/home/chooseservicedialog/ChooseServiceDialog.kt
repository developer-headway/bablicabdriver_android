package com.headway.bablicabdriver.screen.dashboard.home.chooseservicedialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts


@Composable
fun ChooseServiceDialog(
    onDismiss: () -> Unit,
    onOneWaySelected: () -> Unit,
    onShuttleSelected: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MyColors.clr_white_100)
                .padding(20.dp)
        ) {
            // Title
            TextView(
                text = stringResource(R.string.choose_service_for_today),
                textColor = MyColors.clr_132234_100,
                fontFamily = MyFonts.fontBold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            TextView(
                text = stringResource(R.string.you_can_change_anytime),
                textColor = MyColors.clr_607080_100,
                fontFamily = MyFonts.fontRegular,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // One Way option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MyColors.clr_white_100,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MyColors.clr_DCDCDC_100,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TextView(
                        text = stringResource(R.string.one_way),
                        textColor = MyColors.clr_132234_100,
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TextView(
                        text = stringResource(R.string.single_point_to_point),
                        textColor = MyColors.clr_607080_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = MyColors.clr_white_100,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_00BCF1_100,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onOneWaySelected() }
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    TextView(
                        text = stringResource(R.string.set),
                        textColor = MyColors.clr_00BCF1_100,
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Shuttle option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MyColors.clr_E7F4FE_50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onShuttleSelected() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TextView(
                        text = stringResource(R.string.shuttle),
                        textColor = MyColors.clr_132234_100,
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TextView(
                        text = stringResource(R.string.shared_ride_fixed_route),
                        textColor = MyColors.clr_607080_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_next_arrow),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier.size(20.dp),
                    tint = MyColors.clr_132234_100
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            FilledButtonGradient(
                modifier = Modifier
                    .width(100.dp)
                    .align(Alignment.End),
                text = stringResource(R.string.cancel),
                isBorder = true,
                textColor = MyColors.clr_00BCF1_100,
                borderColor = MyColors.clr_00BCF1_100,
                buttonHeight = 38.dp
            ) {
                onDismiss()
            }
        }
    }
}