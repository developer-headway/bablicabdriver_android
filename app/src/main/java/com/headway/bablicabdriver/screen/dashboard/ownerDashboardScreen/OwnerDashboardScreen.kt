package com.headway.bablicabdriver.screen.dashboard.ownerDashboardScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.dashboard.home.HomePageVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.ifEmpty

@Composable
fun OwnerDashboardScreen(navHostController: NavHostController) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }


    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val homePageVm : HomePageVm = viewModel()
    val homePageData by homePageVm.homePageData.collectAsState()
    fun callHomePageApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            homePageVm.callHomePageApi(
                token = token,
                errorStates = errorStates,
                onError = {
                    isRefreshing = false
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    isRefreshing = false
                    if (response?.status == false) {
                        errorStates.bottomToastText.value = response.message
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            networkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    var isFirstTime by rememberSaveable {
        mutableStateOf(true)
    }


    LaunchedEffect(true) {
        Log.d("msg","isFirstTime: $isFirstTime")
        val isRefresh = navHostController.currentBackStackEntry?.savedStateHandle?.get<Boolean?>("refresh")?:false
        if (isFirstTime || isRefresh) {
            navHostController.currentBackStackEntry?.savedStateHandle?.set("refresh",false)
            isFirstTime = false
            callHomePageApi()
        }
    }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            callHomePageApi()
        }
    })
    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .neu(
                        lightShadowColor = MyColors.clr_7E7E7E_13,
                        darkShadowColor = MyColors.clr_7E7E7E_13,
                        shape = Flat(RoundedCorner(12.dp)),
                        shadowElevation = 2.dp
                    )
                    .background(
                        color = MyColors.clr_white_100,
                        shape = RoundedCornerShape( bottomStart = 12.dp,
                            bottomEnd = 12.dp)
                    )
                    .clip(
                        shape = RoundedCornerShape(
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .padding(horizontal = 28.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_logo_main),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier
                        .height(50.dp)
                        .width(130.dp)
                )
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable {
                                navHostController.navigate(Routes.NotificationListScreen.route) {
                                    launchSingleTop = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_notification),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }


                    Spacer(
                        modifier = Modifier
                            .width(12.dp)
                    )
                    var imgLoading by remember {
                        mutableStateOf(true)
                    }
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data(
                                sharedPreferenceManager.getProfilePhoto().ifEmpty {
                                    R.drawable.ic_placeholder
                                }
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(shape = CircleShape)
                            .shimmerEffect(imgLoading)
                            .clickable {
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("show_back_icon",true)
                                navHostController.navigate(Routes.SettingsScreen.route) {
                                    launchSingleTop = true
                                }
                            },
                        contentScale = ContentScale.Crop,
                        onLoading = {
                            imgLoading = true
                        },
                        onSuccess = {
                            imgLoading = false
                        }
                    )

                }

            }
        }
    ) { innerPadding->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pullRefresh(refreshState)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {


                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = MyColors.clr_00BCF1_100,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf( MyColors.clr_white_100,MyColors.clr_D3F5FF_100)
                                )
                            )
                            .clickable {
                                navHostController.navigate(Routes.MyVehicleScreen.route) {
                                    launchSingleTop = true
                                }
                            }
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_car_side),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .size(16.dp)
                            )

                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                            )
                            TextView(
                                text = stringResource(R.string.vehicle),
                                textColor = MyColors.clr_777777_100,
                                fontSize = 14.sp,
                                fontFamily = MyFonts.fontMedium
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .height(7.dp)
                        )
                        TextView(
                            text = "12 Active",
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 20.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )
                        Spacer(
                            modifier = Modifier
                                .height(5.dp)
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_next_double),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.End)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = MyColors.clr_00BCF1_100,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf( MyColors.clr_white_100,MyColors.clr_D3F5FF_100)
                                )
                            )
                            .clickable {
                                navHostController.navigate(Routes.DriverListScreen.route) {
                                    launchSingleTop = true
                                }
                            }
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_driver_man),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .size(16.dp)
                            )

                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                            )
                            TextView(
                                text = stringResource(R.string.driver),
                                textColor = MyColors.clr_777777_100,
                                fontSize = 14.sp,
                                fontFamily = MyFonts.fontMedium
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .height(7.dp)
                        )
                        TextView(
                            text = "12 Active",
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 20.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )
                        Spacer(
                            modifier = Modifier
                                .height(5.dp)
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_next_double),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.End)
                        )
                    }

                }
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = MyColors.clr_00BCF1_100,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(
                                brush = Brush.linearGradient(
                                   colors = listOf( MyColors.clr_white_100,MyColors.clr_D3F5FF_100)
                                )
                            )
                            .clickable {
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("is_today_earning",true)
                                navHostController.navigate(Routes.TransactionsScreen.route) {
                                    launchSingleTop = true
                                }
                            }
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_todays_earning),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .size(16.dp)
                            )

                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                            )
                            TextView(
                                text = stringResource(R.string.today_earning),
                                textColor = MyColors.clr_777777_100,
                                fontSize = 14.sp,
                                fontFamily = MyFonts.fontMedium
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .height(7.dp)
                        )
                        TextView(
                            text = "₹ 2580/-",
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 20.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )
                        Spacer(
                            modifier = Modifier
                                .height(5.dp)
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_next_double),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.End)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = MyColors.clr_00BCF1_100,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf( MyColors.clr_white_100,MyColors.clr_D3F5FF_100)
                                )
                            )
                            .clickable {
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("is_today_earning",false)
                                navHostController.navigate(Routes.TransactionsScreen.route) {
                                    launchSingleTop = true
                                }
                            }
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_total_earning),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .size(16.dp)
                            )

                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                            )
                            TextView(
                                text = stringResource(R.string.total_earning),
                                textColor = MyColors.clr_777777_100,
                                fontSize = 14.sp,
                                fontFamily = MyFonts.fontMedium
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .height(7.dp)
                        )
                        TextView(
                            text = "₹ 2580/-",
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 20.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )
                        Spacer(
                            modifier = Modifier
                                .height(5.dp)
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_next_double),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.End)
                        )
                    }

                }


            }


            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = refreshState
            )

            Image(
                painter = painterResource(R.drawable.ic_background_layer),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .width(260.dp)
                    .height(160.dp)
                    .align(Alignment.BottomStart)
           )
        }

    }


    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError == NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callHomePageApi()
            }
        }
    )

    if (homePageVm._isLoading.collectAsState().value) {
        Loader()
    }
}