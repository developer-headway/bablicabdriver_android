package com.headway.bablicabdriver.screen.dashboard.home.completeRide

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.home.RidePaymentRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.dashboard.home.HomePageVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.RidePaymentVm

@Composable
fun PaymentScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context = context)

    val price = navHostController.previousBackStackEntry?.savedStateHandle?.get<String?>("price")
    val rideId = navHostController.previousBackStackEntry?.savedStateHandle?.get<String?>("ride_id")

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    val ridePaymentVm : RidePaymentVm = viewModel()

    var isCash by remember {
        mutableStateOf(false)
    }

    fun callRidePaymentApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            val request = RidePaymentRequest(
                ride_id = rideId?:"",
                transaction_id = "",
                is_cash = isCash,
                total_amount = price?:""
            )
            ridePaymentVm.callRidePaymentApi(
                token = token,
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    if (response?.status == true) {
                        navHostController.navigate(Routes.PaymentSuccessScreen.route) {
                            launchSingleTop = true
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

    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MyColors.clr_white_100)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                // Amount
                TextView(
                    text = "Payable Amount : 227",
                    textColor = MyColors.clr_364B63_100,
                    fontSize = 16.sp,
                    fontFamily = MyFonts.fontSemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(36.dp))

                Box(
                    modifier = Modifier
                        .size(240.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_qr_border),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .width(26.dp)
                            .height(50.dp)
                            .align(Alignment.TopStart)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_qr_border),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .width(26.dp)
                            .height(50.dp)
                            .rotate(90f)
                            .align(Alignment.TopEnd)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_qr_border),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .width(26.dp)
                            .height(50.dp)
                            .rotate(270f)
                            .align(Alignment.BottomStart)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_qr_border),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .width(26.dp)
                            .height(50.dp)
                            .rotate(180f)
                            .align(Alignment.BottomEnd)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_qr_code),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.Center)
                    )


                }


                Spacer(modifier = Modifier.height(32.dp))

                // OR Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MyColors.clr_00BCF1_100
                    )
                    TextView(
                        text = "OR",
                        textColor = MyColors.clr_black_100,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontFamily = MyFonts.fontBold,
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MyColors.clr_00BCF1_100
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Collect Cash Button
                FilledButtonGradient(
                    text = stringResource(R.string.collect_cash),
                    backgroundColor = MyColors.clr_00BCF1_100,
                    radius = 10.dp,
                    onClick = {

                        isCash = true
                        callRidePaymentApi()
                    }
                )
            }
        }

    }



    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError== NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callRidePaymentApi()
            }

        },
    )

    if (ridePaymentVm._isLoading.collectAsState().value) {
        Loader()
    }



}
