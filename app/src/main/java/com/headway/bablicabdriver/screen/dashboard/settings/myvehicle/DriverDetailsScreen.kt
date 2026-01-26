package com.headway.bablicabdriver.screen.dashboard.settings.myvehicle

import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverDetailsRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.dashboard.settings.myvehicles.DriverDetailsVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverDetailsScreen(
    navHostController: NavHostController,
    driverId: String? = null
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

    val driverDetailsVm : DriverDetailsVm = viewModel()
    val driverDetailsData by driverDetailsVm.driverDetailsData.collectAsState()

    //api call
    fun callDriverDetailsApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val request = DriverDetailsRequest(
                driver_id = driverId?:""
            )
            driverDetailsVm.callDriverDetailsApi(
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
            callDriverDetailsApi()
        }
    }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            callDriverDetailsApi()
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.driver_details),
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
                    VehicleInfoSection(
                        title = stringResource(R.string.email),
                        value = driverDetailsData?.email?:""
                    )
                }
                item {
                    VehicleInfoSection(
                        title = stringResource(R.string.phone),
                        value = driverDetailsData?.phone?:""
                    )
                }
                item {
                    VehicleInfoSection(
                        title = stringResource(R.string.date_of_birth),
                        value = driverDetailsData?.dob?:""
                    )
                }

                item {
                    VehicleInfoSection(
                        title = stringResource(R.string.total_ride),
                        value = "${driverDetailsData?.total_ride?:0}"
                    )
                }


                // Driving Licence Book Image Section
                item {
                    Column {


                        HorizontalDivider(
                            color = MyColors.clr_00BCF1_100
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        VehicleInfoSection(
                            title = stringResource(R.string.driving_licence_number),
                            value = driverDetailsData?.driving_licence_number?:""
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
                                            if (driverDetailsData?.driving_licence_front_side_img_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                driverDetailsData?.driving_licence_front_side_img_url
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
                                            if (driverDetailsData?.driving_licence_back_side_img_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                driverDetailsData?.driving_licence_back_side_img_url
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

                // Aadhar Number Book Image Section
                item {
                    Column {

                        HorizontalDivider(
                            color = MyColors.clr_00BCF1_100
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        VehicleInfoSection(
                            title = stringResource(R.string.aadhar_number),
                            value = driverDetailsData?.aadhar_card_number?:""
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
                                            if (driverDetailsData?.aadhar_card_front_side_img_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                driverDetailsData?.aadhar_card_back_side_img_url
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
                                            if (driverDetailsData?.aadhar_card_back_side_img_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                driverDetailsData?.aadhar_card_back_side_img_url
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


                // Pan Number Book Image Section
                item {
                    Column {
                        HorizontalDivider(
                            color = MyColors.clr_00BCF1_100
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        VehicleInfoSection(
                            title = stringResource(R.string.pan_number),
                            value = driverDetailsData?.pan_card_number?:""
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
                                            if (driverDetailsData?.pan_card_front_side_img_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                driverDetailsData?.pan_card_front_side_img_url
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
                            ){}
                        }
                    }
                }

                // Police Verification Number Book Image Section
                item {
                    Column {
                        HorizontalDivider(
                            color = MyColors.clr_00BCF1_100
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        VehicleInfoSection(
                            title = stringResource(R.string.police_varification),
                            value = driverDetailsData?.pan_card_number?:""
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
                                            if (driverDetailsData?.police_verification_doc_url.isNullOrEmpty()) {
                                                R.drawable.ic_placeholder
                                            } else {
                                                driverDetailsData?.police_verification_doc_url
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
                            ){}
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


    if (driverDetailsVm._isLoading.collectAsState().value) {
        Loader()
    }

    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError== NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callDriverDetailsApi()
            }
        }
    )

}





