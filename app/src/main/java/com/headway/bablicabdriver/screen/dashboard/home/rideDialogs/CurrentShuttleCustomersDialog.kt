package com.headway.bablicabdriver.screen.dashboard.home.rideDialogs


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.model.dashboard.home.CurrentShuttleRidePassenger
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun CurrentShuttleCustomersDialog(
    modifier: Modifier = Modifier,
    list : List<CurrentShuttleRidePassenger?>?,
    finishRideDialog : (String?) -> Unit  = {}
) {
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
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        // Title
        TextView(
            text = stringResource(R.string.current_shuttle_customers),
            textColor = MyColors.clr_132234_100,
            fontFamily = MyFonts.fontBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Trip list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list?:emptyList()) { trip ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_00BCF1_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            TextView(
                                text = "Trip Code: ${trip?.trip_code?:""}",
                                textColor = MyColors.clr_132234_100,
                                fontFamily = MyFonts.fontRegular,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            TextView(
                                text = "No. Of Passengers - ${trip?.passenger_count?:0}",
                                textColor = MyColors.clr_132234_100,
                                fontFamily = MyFonts.fontMedium,
                                fontSize = 14.sp
                            )
                        }

                        FilledButtonGradient(
                            text = stringResource(R.string.finish),
                            textColor = MyColors.clr_white_100,
                            modifier = Modifier
                                .width(90.dp),
                            innerShadow = false,
                            buttonHeight = 36.dp,
                            onClick = {
                                finishRideDialog(trip?.ride_id)
                            }
                        )
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextView(
                            text = trip?.pickup_address?:"",
                            textColor = MyColors.clr_00BCF1_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(180f),
                            tint = MyColors.clr_00BCF1_100
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TextView(
                            text = trip?.drop_address?:"",
                            textColor = MyColors.clr_00BCF1_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }


}