package com.headway.bablicabdriver.screen.dashboard.home.openRides

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.model.dashboard.home.RideRequests
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.viewmodel.dashboard.home.HomePageVm
import kotlinx.coroutines.delay

@Composable
fun RideRequestsDialog(
    homePageVm : HomePageVm,
    onCancelClick: (String?) -> Unit = {},
    onAcceptClick: (String?) -> Unit = {},
) {
    val rideRequestList by homePageVm.rideRequestList.collectAsState()
    Column(
        modifier =  Modifier
            .fillMaxSize()
            .background(
                color = MyColors.clr_white_100
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            items(
                rideRequestList?:emptyList()
            ) { item ->

                var progress by remember { mutableStateOf(0f) }

                val totalTimeMillis: Int = 10_000
                // Launch an effect that updates the progress over time
                LaunchedEffect(Unit) {
                    val duration = totalTimeMillis.toFloat()
                    val frameTime = 16L // ~60fps
                    var elapsed = 0L

                    while (elapsed < totalTimeMillis) {
                        progress = (elapsed / duration).coerceIn(0f, 1f)
                        elapsed += frameTime
                        delay(frameTime)
                    }
                    homePageVm.removeRideRequestList(item)
                    progress = 1f // ensure it completes
                }


                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(10.dp),
                            color = MyColors.clr_00BCF1_100
                        )
                ) {

                    Spacer(
                        modifier = Modifier
                            .height(18.dp)
                    )
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            modifier = Modifier
                                .size(20.dp),
                            contentDescription = stringResource(R.string.img_des),
                            painter = painterResource(R.drawable.ic_one_way)
                        )
                        Text(
                            text = " km (From Your Current) ",
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontSemiBold,
                            color = MyColors.clr_364B63_100
                        )
                    }


                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(18.dp)
                                    .background(
                                        color = MyColors.clr_D3DDE7_100
                                    )
                            )
                            Image(
                                modifier = Modifier
                                    .size(18.dp),
                                painter = painterResource(R.drawable.ic_point_green),
                                contentDescription = stringResource(R.string.img_des)
                            )
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(18.dp)
                                    .background(
                                        color = MyColors.clr_D3DDE7_100
                                    )
                            )

                        }

                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                        )
                        Text(
                            text = item?.pickup_address?:"",
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontRegular,
                            color = MyColors.clr_132234_100,
                        )
                    }


                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            modifier = Modifier
                                .size(20.dp),
                            contentDescription = stringResource(R.string.img_des),
                            painter = painterResource(R.drawable.ic_one_way)
                        )
                        Text(
                            text = "${item?.trip_distance?:""} Km",
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontSemiBold,
                            color = MyColors.clr_364B63_100
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(18.dp)
                                    .background(
                                        color = MyColors.clr_D3DDE7_100
                                    )
                            )
                            Image(
                                modifier = Modifier
                                    .size(18.dp),
                                painter = painterResource(R.drawable.ic_point_red),
                                contentDescription = stringResource(R.string.img_des)
                            )

                        }

                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                        )
                        Text(
                            text = item?.destination_address?:"",
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontRegular,
                            color = MyColors.clr_132234_100
                        )
                    }


                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(8.dp),
                        color = MyColors.clr_00BCF1_100
                    )


                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )


                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        FilledButtonGradient(
                            text = stringResource(R.string.skip_ride),
                            isBorder = true,
                            borderColor = MyColors.clr_00BCF1_100,
                            buttonHeight = 40.dp,
                            textColor = MyColors.clr_00BCF1_100,
                            modifier = Modifier
                                .weight(1f),
                            onClick = {
                                homePageVm.removeRideRequestList(item)
                                onCancelClick(item?.ride_id)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .width(16.dp)
                        )
                        FilledButtonGradient(
                            text = stringResource(R.string.accept_ride),
                            buttonHeight = 40.dp,
                            modifier = Modifier
                                .weight(1f),
                            onClick = {
                                homePageVm.emptyRideRequestList()
                                onAcceptClick(item?.ride_id)
                            }
                        )
                    }


                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )

                }
            }

        }
    }
}

