package com.headway.bablicabdriver.screen.dashboard.settings.bankdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.settings.bankDetails.UpdateBankDetailsRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textfields.FilledTextField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.toast.ToastExpandHorizontal
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.viewmodel.dashboard.settings.bankdetails.GetBankDetailsVm
import com.headway.bablicabdriver.viewmodel.dashboard.settings.bankdetails.UpdateBankDetailsVm

@Composable
fun BankDetailsScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)

    val scope = rememberCoroutineScope()
    val bankName = rememberTextFieldState()
    var bankNameError by rememberSaveable {
        mutableStateOf(false)
    }

    val accountHolderName = rememberTextFieldState()
    var accountHolderNameError by rememberSaveable {
        mutableStateOf(false)
    }

    val accountNumber = rememberTextFieldState()
    var accountNumberError by rememberSaveable {
        mutableStateOf(false)
    }
    val ifscCode = rememberTextFieldState()
    var ifscCodeError by rememberSaveable {
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

    val getBankDetailsVm : GetBankDetailsVm = viewModel()

    fun callGetBankDetailsApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            getBankDetailsVm.callGetBankDetailsApi(
                token = token,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    if (response?.status == true) {
                        val data = response.data
                        bankName.edit {
                            replace(0,length,data.bank_name)
                        }
                        accountHolderName.edit {
                            replace(0,length,data.account_holder_name)
                        }
                        accountNumber.edit {
                            replace(0,length,data.account_no)
                        }
                        ifscCode.edit {
                            replace(0,length,data.ifsc_code)
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
    var updateNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    val updateBankDetailsVm : UpdateBankDetailsVm = viewModel()

    fun callUpdateBankDetailsApi() {
        focusManager.clearFocus(true)
        if (AppUtils.isInternetAvailable(context)) {

            val request = UpdateBankDetailsRequest(
                bank_name = bankName.text.trim().toString(),
                account_holder_name = accountHolderName.text.trim().toString(),
                account_no = accountNumber.text.trim().toString(),
                ifsc_code = ifscCode.text.trim().toString(),
            )
            val token = sharedPreferenceManager.getToken()
            updateBankDetailsVm.callUpdateBankDetailsApi(
                token = token,
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    if (response?.status == true) {
                        val data = response.data
                        mainViewModel.snackBarText.value = response.message
                        mainViewModel.showSnackBar.value = true
                        navHostController.popBackStack()
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            updateNetworkError = NetWorkFail.NetworkError.ordinal
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
            callGetBankDetailsApi()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.bank_details),
                onBackPress = {
                    navHostController.popBackStack()
                }
            )
        },
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
                    text = stringResource(R.string.update),
                    textColor = MyColors.clr_white_100,
                    onClick = {
                        bankNameError = bankName.text.trim().isEmpty()
                        accountHolderNameError = accountHolderName.text.trim().isEmpty()
                        accountNumberError = accountNumber.text.trim().isEmpty()
                        ifscCodeError = ifscCode.text.trim().isEmpty()
                        if (!bankNameError && !accountHolderNameError && !accountNumberError && !ifscCodeError) {
                            callUpdateBankDetailsApi()
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }


        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {


            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                TextView(
                    text = stringResource(R.string.bank_name),
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
                    state = bankName,
                    placeHolder = "",
                    isTyping = {
                        bankNameError = false
                    },
                    borderColor = MyColors.clr_E8E8E8_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    textFontFamily = MyFonts.fontMedium,
                    textColor = MyColors.clr_5A5A5A_100,
                    textFontSize = 14.sp,
                    isLast = false,
                    isTypeNumeric = false
                )

                TextView(
                    text =  if(bankNameError) { stringResource(R.string.this_field_can_not_be_empty) } else "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .padding(horizontal = 20.dp)
                        .height(18.dp),
                    fontSize = 10.sp,
                    fontFamily = MyFonts.fontRegular,
                    textColor = MyColors.clr_FA4949_100
                )


                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                TextView(
                    text = stringResource(R.string.account_holder_name),
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
                    state = accountHolderName,
                    placeHolder = "",
                    isTyping = {
                        accountHolderNameError = false
                    },
                    borderColor = MyColors.clr_E8E8E8_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    textFontFamily = MyFonts.fontMedium,
                    textColor = MyColors.clr_5A5A5A_100,
                    textFontSize = 14.sp,
                    isLast = false,
                    isTypeNumeric = false
                )

                TextView(
                    text =  if(accountHolderNameError) { stringResource(R.string.this_field_can_not_be_empty) } else "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .padding(horizontal = 20.dp)
                        .height(18.dp),
                    fontSize = 10.sp,
                    fontFamily = MyFonts.fontRegular,
                    textColor = MyColors.clr_FA4949_100
                )


                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                TextView(
                    text = stringResource(R.string.account_number),
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
                    state = accountNumber,
                    placeHolder = "",
                    isTyping = {
                        accountNumberError = false
                    },
                    borderColor = MyColors.clr_E8E8E8_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    textFontFamily = MyFonts.fontMedium,
                    textColor = MyColors.clr_5A5A5A_100,
                    textFontSize = 14.sp,
                    isLast = false,
                    isTypeNumeric = false
                )

                TextView(
                    text =  if(accountNumberError) { stringResource(R.string.this_field_can_not_be_empty) } else "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .padding(horizontal = 20.dp)
                        .height(18.dp),
                    fontSize = 10.sp,
                    fontFamily = MyFonts.fontRegular,
                    textColor = MyColors.clr_FA4949_100
                )

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                TextView(
                    text = stringResource(R.string.ifsc_code),
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
                    state = ifscCode,
                    placeHolder = "",
                    isTyping = {
                        ifscCodeError = false
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
                    text =  if(ifscCodeError) { stringResource(R.string.this_field_can_not_be_empty) } else "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .padding(horizontal = 20.dp)
                        .height(18.dp),
                    fontSize = 10.sp,
                    fontFamily = MyFonts.fontRegular,
                    textColor = MyColors.clr_FA4949_100
                )

                Spacer(
                    modifier = Modifier
                        .height(60.dp)
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
                callGetBankDetailsApi()
            }
            if (updateNetworkError== NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                updateNetworkError = NetWorkFail.NoError.ordinal
                callUpdateBankDetailsApi()
            }
        }
    )

    if (getBankDetailsVm._isLoading.collectAsState().value
        || updateBankDetailsVm._isLoading.collectAsState().value) {
        Loader()
    }


}