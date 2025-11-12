package com.headway.bablicabdriver.screen.dashboard.settings.documentinfo

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textfields.FilledTextField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.dashedBorder
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.settings.ProfileVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun DocumentDetailsScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val scope = rememberCoroutineScope()
    val vehicleNumber = rememberTextFieldState()

    var vehicleNumberError by rememberSaveable {
        mutableStateOf(false)
    }

    val type = navHostController.previousBackStackEntry?.savedStateHandle?.get("type")?:""
    val title = when(type) {
        "RC" -> R.string.rc_book_details
        "DL" -> R.string.driving_license_details
        "aadhar" -> R.string.aadhar_details
        "pan" -> R.string.pan_details
        "police_verification" -> R.string.police_varification
        else -> R.string.rc_book_details
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val profileVm : ProfileVm = viewModel()
    val profileData by profileVm.profileData.collectAsState()

    var frontImageUrl by rememberSaveable {
        mutableStateOf("")
    }
    var backImageUrl by rememberSaveable {
        mutableStateOf("")
    }


    fun callProfileApi() {
        if (AppUtils.isInternetAvailable(context)) {

            val token = sharedPreferenceManager.getToken()
            profileVm.callProfileApi(
                token = token,
                errorStates = errorStates,
                onError = {
                    isRefreshing = false
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    isRefreshing = false
                    if (response?.status == true) {
                        Log.d("msg","anca-dxsacsadkjcjsadc-dsajcndsaickm")
                        frontImageUrl = response.data.RCbook_front_photo
                        backImageUrl = response.data.RCbook_back_photo
                        var number = response.data.vehicle_number
                        when(type) {
                            "RC" -> {
                                number = response.data.vehicle_number
                                frontImageUrl = response.data.RCbook_front_photo
                                backImageUrl = response.data.RCbook_back_photo
                            }
                            "DL" -> {
                                number = response.data.dl_number
                                frontImageUrl = response.data.licence_front_photo
                                backImageUrl = response.data.licence_back_photo
                            }
                            "aadhar" -> {
                                number = response.data.aadhar_number
                                frontImageUrl = response.data.aadhar_front_photo
                                backImageUrl = response.data.aadhar_back_photo
                            }
                            "pan" -> {
                                number = response.data.pan_number
                                frontImageUrl = response.data.pan_photo
                            }
                            "police_verification" -> {
                                frontImageUrl = response.data.police_verification_photo
                            }
                            else -> {

                            }
                        }
                        vehicleNumber.edit {
                            replace(0,length, number)
                        }
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


    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            callProfileApi()
        }
    })


    var isFirstTime by rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(
        true
    ) {
        val refresh = navHostController.currentBackStackEntry?.savedStateHandle?.get("refresh")?:false
        if (isFirstTime || refresh) {
            navHostController.currentBackStackEntry?.savedStateHandle?.set("refresh",false)
            isFirstTime = false
            callProfileApi()
        }

    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopNavigationBar(
                title = stringResource(title),
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


            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(
                    modifier = Modifier
                        .height(22.dp)
                )


                when (type) {
                    "DL", "aadhar", "RC", "pan" -> {

                        val title = when(type) {
                            "RC" -> R.string.vehicle_number
                            "DL" -> R.string.driving_license_number
                            "aadhar" -> R.string.aadhar_number
                            "pan" -> R.string.pan_number
                            else -> R.string.vehicle_number
                        }
                        TextView(
                            text = stringResource(title),
                            textColor = MyColors.clr_607080_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontRegular,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                        )

                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )


                        FilledTextField(
                            state = vehicleNumber,
                            placeHolder = stringResource(R.string.enter_number),
                            isTyping = {
                                vehicleNumberError = false
                            },
                            borderColor = MyColors.clr_E8E8E8_100,
                            modifier = Modifier
                                .padding(horizontal = 20.dp),
                            textFontFamily = MyFonts.fontMedium,
                            textColor = MyColors.clr_5A5A5A_100,
                            textFontSize = 14.sp,
                            isLast = true,
                            isTypeNumeric = false
                        )

                        TextView(
                            text =  if(vehicleNumberError) { stringResource(R.string.this_field_can_not_be_empty) } else "",
                            modifier = Modifier
                                .padding(top = 3.dp)
                                .padding(horizontal = 20.dp)
                                .height(18.dp),
                            fontSize = 10.sp,
                            fontFamily = MyFonts.fontRegular,
                            textColor = MyColors.clr_FA4949_100
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier

                            .weight(1f)
                    ) {

                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                        )
                        TextView(
                            text = stringResource(R.string.front_side_photo),
                            textColor = MyColors.clr_607080_100,
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontSemiBold,
                            modifier = Modifier
                        )

                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {


                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .dashedBorder(
                                        width = 1.dp,
                                        color = MyColors.clr_00BCF1_100,
                                        radius = 12.dp
                                    )
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {

                                var imgLoading by remember {
                                    mutableStateOf(true)
                                }
                                AsyncImage(
                                    model = ImageRequest
                                        .Builder(context)
                                        .data(
                                            frontImageUrl
                                        )
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .shimmerEffect(imgLoading),
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

                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )

                    if (type == "pan" || type == "police_verification") {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {}
                    } else {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            Spacer(
                                modifier = Modifier
                                    .height(12.dp)
                            )
                            TextView(
                                text = stringResource(R.string.back_side_photo),
                                textColor = MyColors.clr_607080_100,
                                fontSize = 12.sp,
                                fontFamily = MyFonts.fontSemiBold,
                                modifier = Modifier

                            )

                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {


                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .dashedBorder(
                                            width = 1.dp,
                                            color = MyColors.clr_00BCF1_100,
                                            radius = 12.dp
                                        )
                                        .padding(2.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    var imgLoading by remember {
                                        mutableStateOf(true)
                                    }
                                    AsyncImage(
                                        model = ImageRequest
                                            .Builder(context)
                                            .data(
                                                backImageUrl
                                            )
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .shimmerEffect(imgLoading),
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
                    }




                }


            }

            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = refreshState
            )

        }

    }




    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError==NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callProfileApi()
            }
        },
    )



    if (profileVm._isLoading.collectAsState().value) {
        Loader()
    }




}