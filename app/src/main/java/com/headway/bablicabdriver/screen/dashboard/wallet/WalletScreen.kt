package com.headway.bablicabdriver.screen.dashboard.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.headway.bablicabdriver.model.dashboard.settings.bankDetails.UpdateBankDetailsRequest
import com.headway.bablicabdriver.model.dashboard.wallet.WalletTransactionsRequest
import com.headway.bablicabdriver.model.dashboard.wallet.WithdrawMoneyRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.dialog.WithdrawAmountDialog
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.viewmodel.dashboard.myride.RideHistoryVm
import com.headway.bablicabdriver.viewmodel.dashboard.settings.bankdetails.UpdateBankDetailsVm
import com.headway.bablicabdriver.viewmodel.dashboard.wallet.WalletTransactionsVm
import com.headway.bablicabdriver.viewmodel.dashboard.wallet.WithdrawMoneyVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun WalletScreen(
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

    var showWithdrawAmountDialog = rememberSaveable {
        mutableStateOf(false)
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
//    val errorStates by remember {
//        mutableStateOf(ErrorsData())
//    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val walletTransactionsVm : WalletTransactionsVm = viewModel()
    val transactionList by walletTransactionsVm.transactionList.collectAsState()
    val pagination by walletTransactionsVm.pagination.collectAsState()

    var page by rememberSaveable{
        mutableStateOf(1)
    }

    var walletAmount by rememberSaveable {
        mutableStateOf(0.0)
    }


    fun callWalletTransactionsApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val request = WalletTransactionsRequest(
                page = "$page",
            )
            walletTransactionsVm.callWalletTransactionsApi(
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
                        walletAmount = response.data.total_wallet_balance
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


    var amount by rememberSaveable {
        mutableStateOf("")
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val focusManager = LocalFocusManager.current
    var withdrawNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    val withdrawMoneyVm : WithdrawMoneyVm = viewModel()

    fun callWithdrawMoneyApi() {
        focusManager.clearFocus(true)
        if (AppUtils.isInternetAvailable(context)) {

            val request = WithdrawMoneyRequest(
                amount = amount,
            )
            val token = sharedPreferenceManager.getToken()
            withdrawMoneyVm.callWithdrawMoneyApi(
                token = token,
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    if (response?.status == true) {
                        mainViewModel.snackBarText.value = response.message
                        mainViewModel.showSnackBar.value = true

                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            withdrawNetworkError = NetWorkFail.NetworkError.ordinal
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
            callWalletTransactionsApi()
        }
    }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            callWalletTransactionsApi()
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
                    callWalletTransactionsApi()
                }
            }
    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                showBackIcon = false,
                title = stringResource(R.string.wallet)
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
            ) {


                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp),
                ) {

                    item {


                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
                        ) {

                            Image(
                                painter = painterResource(R.drawable.ic_wallet_frame),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(126.dp),
                                contentScale = ContentScale.FillBounds
                            )

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            ) {
                                TextView(
                                    text = stringResource(R.string.available_balance),
                                    fontSize = 20.sp,
                                    fontFamily = MyFonts.fontSemiBold,
                                    textColor = MyColors.clr_white_100
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(12.dp)
                                )
                                TextView(
                                    text = "₹ $walletAmount",
                                    fontSize = 20.sp,
                                    fontFamily = MyFonts.fontSemiBold,
                                    textColor = MyColors.clr_white_100
                                )
                            }


                        }

                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                        )

                        FilledButtonGradient(
                            modifier = Modifier
                                .padding(horizontal = 12.dp),
                            text = stringResource(R.string.withdraw),
                            isBorder = true,
                            borderColor = MyColors.clr_00BCF1_100,
                            textColor = MyColors.clr_00BCF1_100,
                            textFontSize = 14.sp,
                            onClick = {
//                                navHostController.navigate(Routes.AddMoneyScreen.route) {
//                                    launchSingleTop = true
//                                }
                                showWithdrawAmountDialog.value = true
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                        )

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TextView(
                                text = stringResource(R.string.transactions),
                                fontSize = 14.sp,
                                fontFamily = MyFonts.fontRegular,
                                textColor = MyColors.clr_607080_100
                            )
                        }
                    }

                    items(
                        transactionList?:emptyList()
                    ) { item ->

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
                        ) {
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
                                    painter = painterResource(
                                        when(item?.type) {
                                            "withdraw_request"-> R.drawable.ic_withdrawal
                                            "debit"-> R.drawable.ic_out_wallet
                                            "credit"-> R.drawable.ic_in_wallet
                                            else -> R.drawable.ic_withdrawal
                                        }
                                    ),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .size(24.dp)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .width(20.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    TextView(
                                        text = item?.description?:"",
                                        fontSize = 14.sp,
                                        fontFamily = MyFonts.fontRegular,
                                        textColor = MyColors.clr_364B63_100
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                    )
                                    TextView(
                                        text =item?.date_time?:"",
                                        fontSize = 10.sp,
                                        fontFamily = MyFonts.fontLight,
                                        textColor = MyColors.clr_5A5A5A_100
                                    )
                                }
                                TextView(
                                    text = "${item?.amount ?: 0.0}",
                                    fontSize = 14.sp,
                                    fontFamily = MyFonts.fontRegular,
                                    textColor = MyColors.clr_364B63_100
                                )
                            }

                            Spacer(
                                modifier = Modifier
                                    .height(16.dp)
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = MyColors.clr_C8E0FF_100
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
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


        if (withdrawMoneyVm._isLoading.collectAsState().value
            || walletTransactionsVm._isLoading.collectAsState().value) {
            Loader()
        }
        CommonErrorDialogs(
            showToast = true,
            errorStates = errorStates,
            onNoInternetRetry = {
                if (networkError== NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    networkError = NetWorkFail.NoError.ordinal
                    callWalletTransactionsApi()
                }
                if (withdrawNetworkError== NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    withdrawNetworkError = NetWorkFail.NoError.ordinal
                    callWithdrawMoneyApi()
                }

            }
        )
    }

    if (showWithdrawAmountDialog.value) {
        WithdrawAmountDialog(
            visible = showWithdrawAmountDialog,
            onSubmit = {
                amount = it?:""
                callWithdrawMoneyApi()
            }
        )
    }





}