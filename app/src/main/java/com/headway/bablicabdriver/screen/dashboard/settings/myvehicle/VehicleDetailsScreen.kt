package com.headway.bablicabdriver.screen.dashboard.settings.myvehicle

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.VehicleDetailRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.dashboard.settings.myvehicles.VehicleDetailsVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsScreen(
    navHostController: NavHostController,
    vehicleId: String? = null
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
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }

    val vehicleDetailsVm : VehicleDetailsVm = viewModel()
    val myVehiclesData by vehicleDetailsVm.myVehiclesData.collectAsState()

    //api call
    fun callVehicleDetailsApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val request = VehicleDetailRequest(
                vehicle_id = vehicleId?:""
            )
            vehicleDetailsVm.callVehicleDetailsApi(
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
        if (isFirstTime) {
            isFirstTime = false
            callVehicleDetailsApi()
        }
    }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            callVehicleDetailsApi()
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.vehicle_details),
                onBackPress = {
                    navHostController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(refreshState)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Vehicle Number Section
                item {
                    VehicleInfoSection(
                        title = stringResource(R.string.vehicle_no),
                        value = myVehiclesData?.vehicle_number?:""
                    )
                }

                // Babli Vehicle Code Section
                item {
                    VehicleInfoSection(
                        title = stringResource(R.string.babli_vehicle_code),
                        value = myVehiclesData?.babli_vehicle_code?:""
                    )
                }

                // RC Book Image Section
                item {
                    Column {
                        TextView(
                            text = stringResource(R.string.rc_book_image),
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Front Side
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                TextView(
                                    text = stringResource(R.string.front_side),
                                    textColor = MyColors.clr_132234_100.copy(alpha = 0.6f),
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))



                                var imgLoading by remember {
                                    mutableStateOf(true)
                                }
                                AsyncImage(
                                    model = ImageRequest
                                        .Builder(context)
                                        .data(
                                            if (myVehiclesData?.rc_book_front_side_img_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                myVehiclesData?.rc_book_front_side_img_url
                                            }
                                        )
                                        .build(),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .border(
                                            width = 0.5.dp,
                                            color = MyColors.clr_7E7E7E_25,
                                            shape =  RoundedCornerShape(12.dp)
                                        )
                                        .shimmerEffect(imgLoading),
                                    contentScale = ContentScale.Crop,
                                    onLoading = {
                                        imgLoading = true
                                    },
                                    onSuccess = {
                                        imgLoading = false
                                    },
                                    onError = {
                                        imgLoading = false
                                    }
                                )

                            }

                            // Back Side
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                TextView(
                                    text = stringResource(R.string.back_side),
                                    textColor = MyColors.clr_132234_100.copy(alpha = 0.6f),
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                var imgLoading by remember {
                                    mutableStateOf(true)
                                }
                                AsyncImage(
                                    model = ImageRequest
                                        .Builder(context)
                                        .data(
                                            if (myVehiclesData?.rc_book_back_side_img_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                myVehiclesData?.rc_book_front_side_img_url
                                            }
                                        )
                                        .build(),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .border(
                                            width = 0.5.dp,
                                            color = MyColors.clr_7E7E7E_25,
                                            shape =  RoundedCornerShape(12.dp)
                                        )
                                        .shimmerEffect(imgLoading),
                                    contentScale = ContentScale.Crop,
                                    onLoading = {
                                        imgLoading = true
                                    },
                                    onSuccess = {
                                        imgLoading = false
                                    },
                                    onError = {
                                        imgLoading = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Assigned Driver Section
                item {

                    Column {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = MyColors.clr_00BCF1_100
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        TextView(
                            text = stringResource(R.string.assigned_driver),
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        myVehiclesData?.assigned_driver_for_this_vehicle?.forEach { driver ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                    }
                                    .neu(
                                        lightShadowColor = MyColors.clr_7E7E7E_13,
                                        darkShadowColor = MyColors.clr_7E7E7E_13,
                                        shape = Flat(RoundedCorner(12.dp)),
                                        shadowElevation = 2.dp
                                    )
                                    .background(
                                        color = MyColors.clr_white_100,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
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
                                                if (driver?.driver_image.isNullOrEmpty()) {
                                                    R.drawable.ic_placeholder
                                                } else {
                                                    driver.driver_image
                                                }
                                            )
                                            .build(),
                                        contentDescription = stringResource(R.string.img_des),
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .shimmerEffect(imgLoading),
                                        contentScale = ContentScale.Crop,
                                        onLoading = {
                                            imgLoading = true
                                        },
                                        onSuccess = {
                                            imgLoading = false
                                        },
                                        onError = {
                                            imgLoading = false
                                        }
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                    ) {
                                        TextView(
                                            text = driver?.driver_name?:"",
                                            textColor = MyColors.clr_132234_100,
                                            fontFamily = MyFonts.fontSemiBold,
                                            fontSize = 16.sp
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        TextView(
                                            text = "Shift: ${driver?.driver_shift?:""}",
                                            textColor = MyColors.clr_364B63_100,
                                            fontFamily = MyFonts.fontMedium,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Image(
                                        painter = painterResource(R.drawable.ic_next_arrow),
                                        contentDescription = stringResource(R.string.img_des),
                                        modifier = Modifier
                                            .size(20.dp)
                                    )


                                }

                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_right),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier.size(20.dp),
                                    tint = MyColors.clr_132234_100
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState
            )
        }
    }


    if (vehicleDetailsVm._isLoading.collectAsState().value) {
        Loader()
    }

    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError== NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callVehicleDetailsApi()
            }
        }
    )
}

@Composable
fun VehicleInfoSection(
    title: String,
    value: String
) {
    Column {
        TextView(
            text = title,
            textColor = MyColors.clr_132234_100.copy(alpha = 0.6f),
            fontFamily = MyFonts.fontRegular,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MyColors.clr_F5F5F5_100,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            TextView(
                text = value,
                textColor = MyColors.clr_132234_100,
                fontFamily = MyFonts.fontRegular,
                fontSize = 16.sp
            )
        }
    }
}




