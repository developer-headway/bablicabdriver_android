package com.headway.bablicabdriver.screen.dashboard.myride

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.myride.RideHistoryRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.viewmodel.dashboard.myride.RideHistoryVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun MyRideScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    errorStates: ErrorsData
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    val rideHistoryVm : RideHistoryVm = viewModel()
    val rideDataList by rideHistoryVm.rideDataList.collectAsState()
    val pagination by rideHistoryVm.pagination.collectAsState()

    var page by rememberSaveable{
        mutableStateOf(1)
    }


    //api call
    fun callRideHistoryApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val request = RideHistoryRequest(
                page = "$page",
            )
            rideHistoryVm.callRideHistoryApi(
                token = sharedPreferenceManager.getToken(),
                request = request,
                errorStates = errorStates,
                onError = {
                    isRefreshing = false
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    isRefreshing = false
                    if (response?.status == true) {
                        page = response.data.pagination?.current_page?:1
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
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
        if (isFirstTime) {
            isFirstTime = false
            page = 1
            callRideHistoryApi()
        }
    }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            page = 1
            delay(300)
            callRideHistoryApi()
        }
    })


    val listState = rememberLazyListState()
    LaunchedEffect(listState, pagination) {
        if (pagination == null) return@LaunchedEffect

        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                lastVisibleItemIndex >= totalItems - 2
            }
            .distinctUntilChanged()
            .collect { isAtEnd ->
                if (isAtEnd && page < (pagination?.total_page?:0)) {
                    page += 1
                    callRideHistoryApi()
                }
            }
    }




    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pullRefresh(refreshState)
            ) {
                TopNavigationBar(
                    showBackIcon = false,
                    title = stringResource(R.string.my_ride)
                )
                Row(
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {

                        }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(6.dp)
                    )

                    TextView(
                        text = stringResource(R.string.filter),
                        textColor = MyColors.clr_00BCF1_100,
                        fontFamily = MyFonts.fontMedium,
                        fontSize = 12.sp
                    )

                }
            }

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
            ) {

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(
                        rideDataList?:emptyList()
                    ) { item ->


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
                                    painter = painterResource(R.drawable.ic_auto)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(6.dp)
                                )
                                Text(
                                    text = when(item?.ride_type?.lowercase()) {
                                        "one_way"->"One Way"
                                        "schedule"->"Schedule"
                                        else -> "Shuttle"
                                    },
                                    fontSize = 14.sp,
                                    fontFamily = MyFonts.fontSemiBold,
                                    color = MyColors.clr_364B63_100
                                )

                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = "₹ ${item?.total_price?:0}",
                                    fontSize = 14.sp,
                                    fontFamily = MyFonts.fontSemiBold,
                                    color = MyColors.clr_08875D_100
                                )

                            }

                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
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
                                    text = item?.pickup_address?:"",
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontMedium,
                                    fontSize = 12.sp,
                                    lineHeight = 15.sp,
                                    modifier = Modifier,
                                    maxLines = 3
                                )

                            }
                            Box(
                                modifier = Modifier
                                    .offset(y = -6.dp,)
                                    .padding(start = 21.dp)
                                    .width(2.dp)
                                    .height(28.dp)
                                    .background(
                                        color = MyColors.clr_A6A4A3_100
                                    )
                            )
                            Row(
                                modifier = Modifier
                                    .offset(y = -12.dp,)
                                    .padding(horizontal = 12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(R.drawable.ic_point_red),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(8.dp)
                                )

                                TextView(
                                    text = item?.destination_address?:"",
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontMedium,
                                    fontSize = 12.sp,
                                    lineHeight = 15.sp,
                                    modifier = Modifier,
                                    maxLines = 3
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
                                    painter = painterResource(R.drawable.ic_one_way),
                                    colorFilter = ColorFilter.tint(MyColors.clr_364B63_100)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(6.dp)
                                )
                                Text(
                                    text = "${item?.trip_distance?:0.0} km",
                                    fontSize = 12.sp,
                                    fontFamily = MyFonts.fontMedium,
                                    color = MyColors.clr_364B63_100
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Text(
                                    text = "${item?.ride_date?:""} ${item?.ride_time?:""}",
                                    fontSize = 12.sp,
                                    fontFamily = MyFonts.fontMedium,
                                    color = MyColors.clr_364B63_100
                                )

                            }

                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    modifier = Modifier
                                        .size(18.dp),
                                    contentDescription = stringResource(R.string.img_des),
                                    painter = painterResource(R.drawable.ic_green_check)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(6.dp)
                                )
                                Text(
                                    text = item?.ride_status?:"",
                                    fontSize = 12.sp,
                                    fontFamily = MyFonts.fontMedium,
                                    color = MyColors.clr_08875D_100
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            navHostController.currentBackStackEntry?.savedStateHandle?.set("obj",item)
                                            navHostController.navigate(Routes.MyRideDetailsScreen.route) {
                                                launchSingleTop = true
                                            }
                                        }
                                ) {
                                    Text(
                                        text = stringResource(R.string.view_ride_details),
                                        fontSize = 12.sp,
                                        fontFamily = MyFonts.fontMedium,
                                        color = MyColors.clr_00BCF1_100
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .width(6.dp)
                                    )
                                    Image(
                                        modifier = Modifier
                                            .size(20.dp),
                                        contentDescription = stringResource(R.string.img_des),
                                        painter = painterResource(R.drawable.ic_arrow_right)
                                    )
                                }

                            }

                            Spacer(
                                modifier = Modifier
                                    .height(16.dp)
                            )

                        }

                    }

                    item {
                        if (rideHistoryVm._isLoading.collectAsState().value && page>1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.Center),
                                    color = MyColors.clr_243369_100,
                                    strokeWidth = 3.dp,
                                    trackColor = MyColors.clr_243369_100.copy(alpha = 0.2f)
                                )
                            }

                        }

                    }



                }

            }

            if (rideDataList.isNullOrEmpty() && !rideHistoryVm._isLoading.collectAsState().value) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_no_data),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(100.dp)
                    )
                    TextView(
                        text = stringResource(R.string.no_recode_found),
                        textColor = MyColors.clr_7E7E7E_100,
                        fontFamily = MyFonts.fontMedium,
                        fontSize = 14.sp
                    )
                }

            }


            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = refreshState
            )

        }



        if (rideHistoryVm._isLoading.collectAsState().value && page==1) {
            Loader()
        }
    }


    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError== NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callRideHistoryApi()
            }
        },
    )


}