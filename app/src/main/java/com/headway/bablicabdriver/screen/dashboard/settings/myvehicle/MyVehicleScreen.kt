package com.headway.bablicabdriver.screen.dashboard.settings.myvehicle

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
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
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.dashboard.myride.RideHistoryVm
import com.headway.bablicabdriver.viewmodel.dashboard.settings.myvehicles.MyVehiclesVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVehicleScreen(
    navHostController: NavHostController
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

    val myVehiclesVm : MyVehiclesVm = viewModel()
    val myVehicleList by myVehiclesVm.myVehicleList.collectAsState()

    //api call
    fun callMyVehiclesApi() {
        if (AppUtils.isInternetAvailable(context)) {
            myVehiclesVm.callMyVehiclesApi(
                token = sharedPreferenceManager.getToken(),
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
            callMyVehiclesApi()
        }
    }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            callMyVehiclesApi()
        }
    })


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.my_vehicle),
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
                    .pullRefresh(refreshState),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(myVehicleList?:emptyList()) { item ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navHostController.navigate(
                                        Routes.VehicleDetailsScreen.createRoute(item?.vehicle_id?:"")
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                                .background(
                                    color = MyColors.clr_white_100,
                                    shape = RoundedCornerShape(0.dp)
                                )
                                .padding(vertical = 12.dp)
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                            ) {
                                TextView(
                                    text = item?.vehicle_number?:"",
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 14.sp
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(2.dp)
                                )
                                TextView(
                                    text = "Shift: 10:00 AM - 02:00 PM",
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 12.sp
                                )
                            }


                            Icon(
                                painter = painterResource(R.drawable.ic_next_arrow),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier.size(20.dp),
                                tint = MyColors.clr_132234_100
                            )
                        }
                        HorizontalDivider(
                            color = MyColors.clr_7E7E7E_25,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }


                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState
            )
        }
    }


    if (myVehiclesVm._isLoading.collectAsState().value) {
        Loader()
    }

    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError== NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callMyVehiclesApi()
            }
        }
    )

}




