package com.headway.bablicabdriver.screen.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.BASE_URL
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.api.PRIVACY_POLICY
import com.headway.bablicabdriver.api.TERMS_CONDITION
import com.headway.bablicabdriver.model.login.SendOtpRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textfields.PhoneNumberTextField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.toast.ToastExpandHorizontal
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.login.SendOtpVm

@Composable
fun LoginScreen(navHostController: NavHostController) {
    val context = LocalContext.current

    val phoneNumber = rememberTextFieldState()
    var phoneNumberError by rememberSaveable {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    val sendOtpVm : SendOtpVm = viewModel()

    fun callSendOtpApi() {
        focusManager.clearFocus()
        if (AppUtils.isInternetAvailable(context)) {
            val request = SendOtpRequest(
                phone_number = phoneNumber.text.toString()
            )
            sendOtpVm.callSendOtpApi(
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    if (response?.status == true) {
                        AppUtils.showToast(
                            context = context,
                            message = response.data.otp
                        )
                        navHostController.currentBackStackEntry?.savedStateHandle?.set("mobile_number",phoneNumber.text.toString())
                        navHostController.navigate(Routes.OTPVerificationScreen.route){
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
                    text = stringResource(R.string.get_verification_code),
                    textColor = MyColors.clr_white_100,
                    onClick = {
                        phoneNumberError = phoneNumber.text.isEmpty() || phoneNumber.text.length<=9
                        if (!phoneNumberError) {
                            callSendOtpApi()
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
        containerColor = MyColors.clr_white_100,

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {



            Spacer(
                modifier = Modifier
                    .height(76.dp)
            )

            Image(
                painter = painterResource(R.drawable.ic_splash_logo),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .width(120.dp)
                    .height(46.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            TextView(
                text = stringResource(R.string.enter_phone_number),
                textColor = MyColors.clr_132234_100,
                fontSize = 18.sp,
                fontFamily = MyFonts.fontSemiBold,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            TextView(
                text = stringResource(R.string.well_text_a_code),
                textColor = MyColors.clr_364B63_100,
                fontSize = 12.sp,
                fontFamily = MyFonts.fontRegular,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(22.dp)
            )



            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    modifier = Modifier
                        .height(44.dp)
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_E8E8E8_100,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_flag),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(6.dp)
                    )

                    TextView(
                        text = "+91",
                        textColor = MyColors.clr_313131_100,
                        fontSize = 14.sp,
                        fontFamily = MyFonts.fontRegular,
                        modifier = Modifier
                    )
                    Spacer(
                        modifier = Modifier
                            .width(4.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_drop_down),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(12.dp)
                    )
                }


                PhoneNumberTextField(
                    state = phoneNumber,
                    isTyping = {
                        phoneNumberError = false
                    },
                    placeHolder = stringResource(R.string.phone_number),
                    borderColor = MyColors.clr_E8E8E8_100,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    textFontFamily = MyFonts.fontRegular,
                    textColor = MyColors.clr_313131_100,
                    textFontSize = 14.sp,
                    isLast = true,
                    isTypeNumeric = true,
                    showHeading = false
                )
            }


            Spacer(
                modifier = Modifier
                    .height(3.dp)
            )

            TextView(
                text =  if(phoneNumberError) {
                    if (phoneNumber.text.isNotEmpty()) stringResource(R.string.kindly_enter_valid_mobile) else  stringResource(R.string.this_field_can_not_be_empty) }
                else "",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(18.dp),
                fontSize = 10.sp,
                fontFamily = MyFonts.fontRegular,
                textColor = MyColors.clr_FA4949_100

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
                callSendOtpApi()
            }
        },
    )



    if (sendOtpVm._isLoading.collectAsState().value) {
        Loader()
    }


}