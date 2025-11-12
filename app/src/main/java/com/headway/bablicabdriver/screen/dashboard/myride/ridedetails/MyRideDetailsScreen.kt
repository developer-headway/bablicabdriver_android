package com.headway.bablicabdriver.screen.dashboard.myride.ridedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.myride.RideData
import com.headway.bablicabdriver.res.components.bar.StarRatingBar
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils.makePhoneCall
import com.headway.bablicabdriver.utils.shimmerEffect
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyRideDetailsScreen(
    navHostController: NavHostController
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val rideData = navHostController.previousBackStackEntry?.savedStateHandle?.get<RideData?>("obj")
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            isRefreshing = false
        }
    })
    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.ride_details),
                onBackPress = {
                    navHostController.popBackStack()
                }
            )
        }
    ) { innerPadding ->


        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pullRefresh(refreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {



                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
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
                            .width(8.dp)
                    )

                    TextView(
                        text = rideData?.pickup_address?:"",
                        textColor = MyColors.clr_132234_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 14.sp,
                        modifier = Modifier,
                        maxLines = 2
                    )

                }
                Box(
                    modifier = Modifier
                        .offset(y = -6.dp,)
                        .padding(start = 37.dp)
                        .width(2.dp)
                        .height(28.dp)
                        .background(
                            color = MyColors.clr_A6A4A3_100
                        )
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .offset(y = -12.dp,)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_point_red),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
//                                        .align(Alignment.Top)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(8.dp)
                    )

                    TextView(
                        text = rideData?.destination_address?:"",
                        textColor = MyColors.clr_132234_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 14.sp,
                        modifier = Modifier,
                        maxLines = 2
                    )

                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    color = MyColors.clr_00BCF1_100
                )



                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextView(
                        text = stringResource(R.string.date_time),
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    TextView(
                        text = "${rideData?.ride_date?:""} ${rideData?.ride_time?:""}",
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )


                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextView(
                        text = stringResource(R.string.ride_type),
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    TextView(
                        text = rideData?.ride_type?:"",
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextView(
                        text = stringResource(R.string.payment_type),
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    TextView(
                        text = rideData?.booking_type?:"",
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(22.dp)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    color = MyColors.clr_00BCF1_100
                )





                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextView(
                        text = stringResource(R.string.total_distance),
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    TextView(
                        text = "${rideData?.trip_distance?:0.0} KM",
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )



                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextView(
                        text = stringResource(R.string.ride_status),
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    TextView(
                        text = rideData?.ride_status?:"",
                        textColor = MyColors.clr_08875D_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                }


                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextView(
                        text = stringResource(R.string.your_payment),
                        textColor = MyColors.clr_282F39_100,
                        fontSize = 16.sp,
                        fontFamily = MyFonts.fontSemiBold
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    TextView(
                        text = "₹ ${rideData?.total_price?:0}",
                        textColor = MyColors.clr_08875D_100,
                        fontSize = 16.sp,
                        fontFamily = MyFonts.fontSemiBold
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(22.dp)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    color = MyColors.clr_00BCF1_100
                )


                Spacer(
                    modifier = Modifier
                        .height(22.dp)
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
                                if (rideData?.customer_profile_image.isNullOrEmpty()) {
                                    R.drawable.ic_placeholder
                                } else {
                                    rideData.customer_profile_image
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
                        text = rideData?.customer_name?:"",
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 14.sp,
                        textColor = MyColors.clr_black_100,
                    )

                }


                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )



                if ((rideData?.star_rating ?: 0f) > 1f) {
                    StarRatingBar(
                        modifier = Modifier
                            .padding(horizontal = 28.dp),
                        rating = rideData?.star_rating?:0f
                    ) {

                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    TextView(
                        modifier = Modifier
                            .padding(horizontal = 28.dp),
                        text = rideData?.comment?:"",
                        textColor = MyColors.clr_08875D_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular
                    )
                }



            }

            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = refreshState
            )
        }


    }



}