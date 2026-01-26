package com.headway.bablicabdriver.screen.login

import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.BASE_URL
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.api.PRIVACY_POLICY
import com.headway.bablicabdriver.api.TERMS_CONDITION
import com.headway.bablicabdriver.model.login.VerifyOtpRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textfields.OtpInputField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.toast.ToastExpandHorizontal
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.login.VerifyOtpVm
import kotlinx.coroutines.delay

@Composable
fun OTPVerificationScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val focusManager = LocalFocusManager.current
    val mobileNum = navHostController.previousBackStackEntry?.savedStateHandle?.get<String>("mobile_number")
    val otp = rememberSaveable {
        mutableStateOf("")
    }
    var otpError by rememberSaveable {
        mutableStateOf(false)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    var isFromStatus by remember {
        mutableStateOf("")
    }

    var otpTime by rememberSaveable {
        mutableStateOf(60)
    }
    var isTimerRunning by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(
        isTimerRunning
    ) {
        while (otpTime > 0) {
            delay(1000L)
            otpTime--
        }
        isTimerRunning = false

    }
    var deviceFcmToken by rememberSaveable {
        mutableStateOf("")
    }
    fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            deviceFcmToken = task.result
            // Log and toast
            val msg = "fcm token :${deviceFcmToken}"
        })
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    val verifyOtpVm : VerifyOtpVm = viewModel()

    fun callVerifyOtpApi() {
        focusManager.clearFocus()
        if (AppUtils.isInternetAvailable(context)) {

            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            val request = VerifyOtpRequest(
                phone_number = mobileNum ?: "",
                device_uid = deviceId,
                device_token = deviceFcmToken,
                otp = otp.value
            )
            verifyOtpVm.callVerifyOtpApi(
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    if (response?.status == true) {
                        navHostController.currentBackStackEntry?.savedStateHandle?.set("mobile_number",mobileNum)


                        val data = response.data

                        if (data.is_approved) {
                            sharedPreferenceManager.setIsLogin(true)
                            sharedPreferenceManager.storeVerifyOtpData(response.data)
                            navHostController.navigate(Routes.DashboardScreen.route) {
                                launchSingleTop = true
                            }
                        } else {
                            if (data.is_Pan_completed==1 &&
                                data.is_RCbook_completed==1 &&
                                data.is_licence_completed==1 &&
                                data.is_Aadhar_completed==1 &&
                                data.is_police_verification_completed==1 &&
                                data.is_profile_completed==1
                            ) {
                                navHostController.navigate(Routes.WaitingScreen.route) {
                                    launchSingleTop = true
                                    popUpTo(Routes.OTPVerificationScreen.route) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                sharedPreferenceManager.storeVerifyOtpData(response.data)
                                navHostController.navigate(Routes.RegistrationScreen.route) {
                                    launchSingleTop = true
                                    popUpTo(Routes.OTPVerificationScreen.route) {
                                        inclusive = true
                                    }
                                }
                            }
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
    DisposableEffect(key1 = Unit) {
        getFCMToken()
        onDispose {  }
    }

    LaunchedEffect(otp.value) {
        if (otp.value.length>3) {
            callVerifyOtpApi()
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MyColors.clr_white_100,
                    )
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ToastExpandHorizontal(
                    showBottomToast = errorStates.showBottomToast,
                    s = errorStates.bottomToastText.value
                )
                FilledButtonGradient(
                    text = stringResource(R.string.verify),
                    textColor = MyColors.clr_white_100,
                    onClick = {
                        otpError = otp.value.isEmpty() || otp.value.length<4

                        if (!otpError) {
                            callVerifyOtpApi()
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )



                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )



                val annotatedText = buildAnnotatedString {
                    append(stringResource(R.string.by_continuing))
                    append(" ")
                    // Add annotation for "Terms and Conditions"
                    pushStringAnnotation(tag = "TERMS", annotation = "terms")
                    withStyle(
                        style = SpanStyle(
                            color = MyColors.clr_243369_100,
                            fontFamily = MyFonts.fontRegular
                        )
                    ) {
                        append(stringResource(R.string.t_c))
                    }
                    pop()

                    append(" and ")

                    // Add annotation for "Privacy Policy"
                    pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                    withStyle(style = SpanStyle(color = MyColors.clr_243369_100, fontFamily = MyFonts.fontRegular)) {
                        append(stringResource(R.string.privacy_policy))
                    }
                    pop()
                }


                ClickableText(
                    text = annotatedText,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 12.sp,
                        color = MyColors.clr_5A5A5A_100
                    ),
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                            .firstOrNull()?.let {
                                // Handle Terms click
                                Log.d("ClickableText", "Clicked on Terms and Conditions")
                                val url = BASE_URL + TERMS_CONDITION
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("title", "Terms & Conditions")
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("url", url)
                                navHostController.navigate(Routes.WebPageScreen.route) {
                                    launchSingleTop = true
                                }
                            }

                        annotatedText.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                            .firstOrNull()?.let {
                                // Handle Privacy click
                                Log.d("ClickableText", "Clicked on Privacy Policy")
                                val url = BASE_URL + PRIVACY_POLICY
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("title", "Privacy Policy")
                                navHostController.currentBackStackEntry?.savedStateHandle?.set("url", url)
                                navHostController.navigate(Routes.WebPageScreen.route) {
                                    launchSingleTop = true
                                }
                            }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
            }
        },
        containerColor = MyColors.clr_white_100
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(
                modifier = Modifier
                    .height(76.dp)
            )

            TextView(
                text = stringResource(R.string.otp_verification),
                textColor = MyColors.clr_black_100,
                fontSize = 28.sp,
                fontFamily = MyFonts.fontBold,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )
            TextView(
                text = stringResource(R.string.enter_the_verification),
                textColor = MyColors.clr_707070_100,
                fontSize = 14.sp,
                fontFamily = MyFonts.fontMedium,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )


            Spacer(
                modifier = Modifier
                    .height(44.dp)

            )


            OtpInputField(
                otp,
                isTyping = {
                    otpError = false
                }
            )

            Spacer(
                modifier = Modifier
                    .height(3.dp)
            )

            TextView(
                text =  if(otpError) {
                    if (otp.value.isNotEmpty()) stringResource(R.string.kindly_enter_valid_otp) else  stringResource(R.string.this_field_can_not_be_empty) }
                else "",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(18.dp),
                fontSize = 10.sp,
                fontFamily = MyFonts.fontRegular,
                textColor = MyColors.clr_FA4949_100

            )

            Spacer(
                modifier = Modifier
                    .height(46.dp)
            )
            TextView(
                text = "${stringResource(R.string.resend_otp_in)} ${otpTime}s",
                textColor = MyColors.clr_243369_100,
                fontSize = 14.sp,
                fontFamily = MyFonts.fontSemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )


            TextView(
                text = stringResource(R.string.resend_otp),
                textColor = if (isTimerRunning) MyColors.clr_707070_100 else MyColors.clr_243369_100,
                fontSize = 14.sp,
                fontFamily = MyFonts.fontSemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        if (!isTimerRunning) {
                            otpTime = 60
                            isTimerRunning = true
                        }
                    }
                    .padding(5.dp)
            )


            Spacer(
                modifier = Modifier
                    .height(50.dp)
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
                callVerifyOtpApi()
            }
        },
    )



    if (verifyOtpVm._isLoading.collectAsState().value) {
        Loader()
    }



}