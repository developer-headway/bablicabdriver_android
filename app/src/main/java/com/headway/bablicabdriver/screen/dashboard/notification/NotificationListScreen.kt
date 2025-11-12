package com.headway.bablicabdriver.screen.dashboard.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.notifocation.NotificationsRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.dashboard.NotificationListVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun NotificationListScreen(
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val notificationListVm : NotificationListVm = viewModel()
    val notificationList by notificationListVm.notificationList.collectAsState()
    val pagination by notificationListVm.pagination.collectAsState()

    var page by rememberSaveable{
        mutableStateOf(1)
    }



    fun callRideHistoryApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val request = NotificationsRequest(
                page = "$page",
            )
            notificationListVm.callNotificationsApi(
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
            page = 1
            isRefreshing = true
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
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                showBackIcon = true,
                title = stringResource(R.string.notification),
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        notificationList?:emptyList()
                    ) { item ->

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
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
                                .padding(10.dp)
                        ) {
                            TextView(
                                text = item?.title?:"",
                                fontSize = 14.sp,
                                fontFamily = MyFonts.fontSemiBold,
                                textColor = MyColors.clr_black_100
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(8.dp)
                            )
                            TextView(
                                text = item?.message?:"",
                                fontSize = 12.sp,
                                fontFamily = MyFonts.fontRegular,
                                textColor = MyColors.clr_364B63_100
                            )
                            TextView(
                                text = item?.date_time?:"",
                                fontSize = 14.sp,
                                fontFamily = MyFonts.fontRegular,
                                textColor = MyColors.clr_black_100,
                                modifier = Modifier
                                    .align(Alignment.End)
                            )
                        }
                    }

                    item {
                        if (notificationListVm._isLoading.collectAsState().value && page>1) {
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


            if (notificationList.isNullOrEmpty() && !notificationListVm._isLoading.collectAsState().value) {
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

    if (notificationListVm._isLoading.collectAsState().value && page==1) {
        Loader()
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
        }
    )

}