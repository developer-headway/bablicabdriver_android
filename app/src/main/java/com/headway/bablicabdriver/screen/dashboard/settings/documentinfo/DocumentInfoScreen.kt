package com.headway.bablicabdriver.screen.dashboard.settings.documentinfo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.CornerShape
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.registration.DocumentData
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.registration.RegistrationDetailsVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.PullRefreshLayout
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DocumentInfoScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val scope = rememberCoroutineScope()

    val list = remember {
        mutableStateListOf(
            R.string.rc_book_details,
            R.string.driving_license,
            R.string.aadhar_card,
            R.string.pan_card,
            R.string.police_varification,
        )
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

    val registrationDetailsVm : RegistrationDetailsVm = viewModel()

    val registrationDetailsData by registrationDetailsVm.registrationDetailsData.collectAsState()
    fun callRegistrationDetailsApi() {
        if (AppUtils.isInternetAvailable(context)) {

            val token = sharedPreferenceManager.getToken()
            registrationDetailsVm.callRegistrationDetailsApi(
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
            callRegistrationDetailsApi()
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
            callRegistrationDetailsApi()
        }

    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.document_info),
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(
                        list
                    ) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .neu(
                                    shadowElevation = 2.dp,
                                    lightShadowColor = MyColors.clr_7E7E7E_13,
                                    darkShadowColor = MyColors.clr_7E7E7E_13,
                                    shape = Flat(RoundedCorner(10.dp))
                                )
                                .background(
                                    color = MyColors.clr_white_100,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {


                                    val type = when(item) {
                                        R.string.rc_book -> { "RC" }
                                        R.string.driving_license -> { "DL" }
                                        R.string.aadhar_card -> { "aadhar" }
                                        R.string.pan_card -> { "pan" }
                                        R.string.police_varification -> { "police_verification" }
                                        else -> "RC"
                                    }
                                    navHostController.currentBackStackEntry?.savedStateHandle?.set("type",type)
                                    navHostController.navigate(Routes.DocumentDetailsScreen.route) {
                                        launchSingleTop = true
                                    }
                                }
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TextView(
                                text = stringResource(item),
                                textColor = MyColors.clr_364B63_100,
                                fontFamily = MyFonts.fontRegular,
                                fontSize = 14.sp
                            )

                            Image(
                                painter = painterResource(R.drawable.ic_next_arrow),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .size(20.dp)
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

    }




    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError==NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callRegistrationDetailsApi()
            }
        },
    )



    if (registrationDetailsVm._isLoading.collectAsState().value) {
        Loader()
    }




}