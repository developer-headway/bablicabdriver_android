package com.headway.bablicabdriver.screen.dashboard.home.rideDialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.model.dashboard.home.CurrentRide
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.AppUtils.makePhoneCall
import com.headway.bablicabdriver.utils.AppUtils.openGoogleMap
import com.headway.bablicabdriver.utils.shimmerEffect
import kotlin.text.ifEmpty

@Composable
fun PickupDialog(
    modifier: Modifier = Modifier,
    currentRide: CurrentRide?,
    onArrivedClick : () -> Unit  = {}
) {
    val context = LocalContext.current
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
            text = stringResource(R.string.pickup),
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

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.ic_point_green),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .size(20.dp)
            )

            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )

            TextView(
                text = stringResource(R.string.pickup_address),
                fontFamily = MyFonts.fontSemiBold,
                fontSize = 14.sp,
                textColor = MyColors.clr_black_100,
            )


        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        Row(
            modifier = Modifier
                .padding(start = 46.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                text = currentRide?.pickup_address?:"",
                fontFamily = MyFonts.fontRegular,
                fontSize = 14.sp,
                textColor = MyColors.clr_black_100,
                modifier = Modifier
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .border(
                        width = 1.dp,
                        color = MyColors.clr_00BCF1_100,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        openGoogleMap(
                            context = context,
                            latitude = currentRide?.pickup_Latitude?:0.0,
                            longitude = currentRide?.pickup_Longitude?:0.0,
                        )
                    }
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                TextView(
                    text = "Go To\nPickup",
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 14.sp,
                    textColor = MyColors.clr_00BCF1_100,
                )
            }
        }


        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        HorizontalDivider(
            color = MyColors.clr_00BCF1_100,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(
            modifier = Modifier
                .height(12.dp)
        )

        TextView(
            text = stringResource(R.string.user_details),
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

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            var imgLoading by remember {
                mutableStateOf(true)
            }
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(
                        if (currentRide?.customer_profile_image.isNullOrEmpty()) {
                            R.drawable.ic_placeholder
                        } else {
                            currentRide.customer_profile_image
                        }
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .shimmerEffect(imgLoading),
                contentScale = ContentScale.Crop,
                onLoading = {
                    imgLoading = true
                },
                onSuccess = {
                    imgLoading = false
                }
            )

            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )

            TextView(
                text = currentRide?.customer_name?:"",
                fontFamily = MyFonts.fontRegular,
                fontSize = 14.sp,
                textColor = MyColors.clr_black_100,
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .border(
                        width = 1.dp,
                        color = MyColors.clr_00BCF1_100,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        makePhoneCall(context = context, phoneNumber = currentRide?.customer_phone_number?:"")
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                TextView(
                    text = "Call",
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 14.sp,
                    textColor = MyColors.clr_00BCF1_100,
                )
            }

        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        HorizontalDivider(
            color = MyColors.clr_00BCF1_100,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        FilledButtonGradient(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.arrived_at_pickup),
            maxWidth = true,
            onClick = {
                onArrivedClick()
            }
        )
        Spacer(
            modifier = Modifier
                .height(30.dp)
        )



    }
}

