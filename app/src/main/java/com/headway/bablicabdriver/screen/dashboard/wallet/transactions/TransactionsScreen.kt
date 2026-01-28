package com.headway.bablicabdriver.screen.dashboard.wallet.transactions

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.wallet.WalletTransactionsRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.dashboard.wallet.WalletTransactionsVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone


@Composable
fun TransactionsScreen(
    navHostController: NavHostController,
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val isTodayEarning = navHostController.previousBackStackEntry?.savedStateHandle?.get<Boolean?>("is_today_earning")?:false

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    val date = rememberSaveable {
        mutableStateOf("")
    }
    var dateError by rememberSaveable {
        mutableStateOf(false)
    }
    fun datePicker(selDate: MutableState<String>, onResult:() ->Unit = {}) {
        var month = ""
        var day = ""
        val c = Calendar.getInstance()
        val splitDate = selDate.value.split(" ")
        val mYear = if (selDate.value.isEmpty()) c[Calendar.YEAR] else splitDate[2].toInt()
        val mMonth = if (selDate.value.isEmpty()) c[Calendar.MONTH] else AppUtils.calendarList.indexOf(splitDate[1])
        val mDay = if (selDate.value.isEmpty()) c[Calendar.DAY_OF_MONTH] else splitDate[0].toInt()

        val datePickerDialog = DatePickerDialog(
            context, R.style.DialogTheme,
            { _, year, monthOfYear1, dayOfMonth ->
                var monthOfYear = monthOfYear1
                monthOfYear += 1
                month = AppUtils.calendarList[monthOfYear - 1]
                day = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                selDate.value = "$day $month $year"
                onResult()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = Calendar.getInstance(TimeZone.getDefault()).timeInMillis
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor("#FF2096F3".toColorInt())
    }



    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val walletTransactionsVm : WalletTransactionsVm = viewModel()
    val transactionList by walletTransactionsVm.transactionList.collectAsState()
    val pagination by walletTransactionsVm.pagination.collectAsState()

    var page by rememberSaveable{
        mutableStateOf(1)
    }


    fun callWalletTransactionsApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val request = WalletTransactionsRequest(
                page = "$page",
                filter_date = AppUtils.convertDateFormat(date.value) ?: ""
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

            val currentData = AppUtils.getCurrentDate("dd MMM yyyy")
            date.value = if (isTodayEarning) "$currentData" else ""
            callWalletTransactionsApi()
        }
    }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            page = 1
            date.value = ""
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
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                TopNavigationBar(
                    title = stringResource(R.string.transactions),
                    onBackPress = {
                        navHostController.popBackStack()
                    }
                )
                Row(
                    modifier = Modifier
                        .padding(end = 14.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            datePicker(date) {
                                dateError = false
                                page = 1
                                callWalletTransactionsApi()
                            }
                        }
                        .padding(6.dp),
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
                            .height(6.dp)
                    )
                    TextView(
                        text = stringResource(R.string.filter),
                        fontSize = 12.sp,
                        fontFamily = MyFonts.fontRegular,
                        textColor = MyColors.clr_00BCF1_100
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
                ) {
                    items(
                        transactionList?:emptyList()
                    ) { item ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {}
                                .padding(horizontal = 16.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 0.dp)
                                    .fillMaxWidth()

                                    .padding(top = 8.dp, bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(
                                        when(item?.type) {
                                            "debit"-> R.drawable.ic_out_wallet
                                            "credit"-> R.drawable.ic_in_wallet
                                            else -> R.drawable.ic_out_wallet
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
                                        fontFamily = MyFonts.fontMedium,
                                        textColor = MyColors.clr_364B63_100,
                                        maxLines = 1
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(6.dp)
                                    )
                                    TextView(
                                        text =item?.date_time?:"",
                                        fontSize = 12.sp,
                                        fontFamily = MyFonts.fontMedium,
                                        textColor = MyColors.clr_364B63_100
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                )
                                TextView(
                                    text = "${AppUtils.currency}${item?.amount ?: 0.0}",
                                    fontSize = 14.sp,
                                    fontFamily = MyFonts.fontRegular,
                                    textColor = MyColors.clr_364B63_100
                                )
                            }

//                            Spacer(
//                                modifier = Modifier
//                                    .height(16.dp)
//                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = MyColors.clr_C8E0FF_100
                            )
//                            Spacer(
//                                modifier = Modifier
//                                    .height(10.dp)
//                            )
                        }


                    }
                }

            }

            if (transactionList.isNullOrEmpty() && !walletTransactionsVm._isLoading.collectAsState().value) {
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
    }



    if (walletTransactionsVm._isLoading.collectAsState().value) {
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

        }
    )


}

