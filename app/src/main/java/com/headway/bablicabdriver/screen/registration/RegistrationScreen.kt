package com.headway.bablicabdriver.screen.registration

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
fun RegistrationScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val scope = rememberCoroutineScope()
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
                title = stringResource(R.string.registration),
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
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_D3DDE7_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            when(registrationDetailsData?.profile_completed_status){
                                "not_submitted","rejected" -> {
                                    navHostController.currentBackStackEntry?.savedStateHandle?.set("is_edit",false)
                                    navHostController.navigate(Routes.ProfileScreen.route) {
                                        launchSingleTop = true
                                    }
                                }
                                else -> {}
                            }

                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column{

                        TextView(
                            text = stringResource(R.string.profile),
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )

                        Spacer(
                            modifier = Modifier
                                .height(6.dp)
                        )

                        TextView(
                            text = when(registrationDetailsData?.profile_completed_status){
                                "not_submitted" -> "Not Submitted"
                                "submitted" -> "Submitted"
                                "approved" -> "Approved"
                                "rejected" -> "Rejected"
                                else -> ""
                            },
                            textColor = when(registrationDetailsData?.profile_completed_status){
                                "not_submitted" -> MyColors.clr_ef872f_100
                                "submitted" -> MyColors.clr_00BCF1_100
                                "approved" -> MyColors.clr_36C10C_100
                                "rejected" -> MyColors.clr_CF0000_100
                                else -> MyColors.clr_36C10C_100
                            },
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontRegular
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_next_arrow),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_D3DDE7_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            when(registrationDetailsData?.RCBook_completed_status) {
                                "not_submitted","rejected" -> {
                                    if (registrationDetailsData?.profile_completed_status!="not_submitted") {
                                        navHostController.currentBackStackEntry?.savedStateHandle?.set("type","RC")
                                        navHostController.navigate(Routes.RCBookDetailsScreen.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column{

                        TextView(
                            text = stringResource(R.string.rc_book_details),
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )

                        Spacer(
                            modifier = Modifier
                                .height(6.dp)
                        )

                        TextView(
                            text = when(registrationDetailsData?.RCBook_completed_status){
                                "not_submitted" -> "Not Submitted"
                                "submitted" -> "Submitted"
                                "approved" -> "Approved"
                                "rejected" -> "Rejected"
                                else -> ""
                            },
                            textColor = when(registrationDetailsData?.RCBook_completed_status){
                                "not_submitted" -> MyColors.clr_ef872f_100
                                "submitted" -> MyColors.clr_00BCF1_100
                                "approved" -> MyColors.clr_36C10C_100
                                "rejected" -> MyColors.clr_CF0000_100
                                else -> MyColors.clr_36C10C_100
                            },
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontRegular
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_next_arrow),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_D3DDE7_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {

                            when(registrationDetailsData?.licence_completed_status) {
                                "not_submitted","rejected" -> {
                                    if (registrationDetailsData?.profile_completed_status=="submitted" &&
                                        registrationDetailsData?.RCBook_completed_status=="submitted") {
                                        navHostController.currentBackStackEntry?.savedStateHandle?.set("type","DL")
                                        navHostController.navigate(Routes.RCBookDetailsScreen.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                                else -> {}
                            }

                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column{

                        TextView(
                            text = stringResource(R.string.driving_license_details),
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )

                        Spacer(
                            modifier = Modifier
                                .height(6.dp)
                        )

                        TextView(
                            text = when(registrationDetailsData?.licence_completed_status){
                                "not_submitted" -> "Not Submitted"
                                "submitted" -> "Submitted"
                                "approved" -> "Approved"
                                "rejected" -> "Rejected"
                                else -> ""
                            },
                            textColor = when(registrationDetailsData?.licence_completed_status){
                                "not_submitted" -> MyColors.clr_ef872f_100
                                "submitted" -> MyColors.clr_00BCF1_100
                                "approved" -> MyColors.clr_36C10C_100
                                "rejected" -> MyColors.clr_CF0000_100
                                else -> MyColors.clr_36C10C_100
                            },
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontRegular
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_next_arrow),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }


                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_D3DDE7_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            when(registrationDetailsData?.aadhar_completed_status) {
                                "not_submitted","rejected" -> {
                                    if (registrationDetailsData?.profile_completed_status=="submitted" &&
                                        registrationDetailsData?.RCBook_completed_status=="submitted" &&
                                        registrationDetailsData?.licence_completed_status=="submitted" ) {
                                        navHostController.currentBackStackEntry?.savedStateHandle?.set("type","aadhar")
                                        navHostController.navigate(Routes.RCBookDetailsScreen.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column{

                        TextView(
                            text = stringResource(R.string.aadhar_details),
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )

                        Spacer(
                            modifier = Modifier
                                .height(6.dp)
                        )

                        TextView(
                            text = when(registrationDetailsData?.aadhar_completed_status){
                                "not_submitted" -> "Not Submitted"
                                "submitted" -> "Submitted"
                                "approved" -> "Approved"
                                "rejected" -> "Rejected"
                                else -> ""
                            },
                            textColor = when(registrationDetailsData?.aadhar_completed_status){
                                "not_submitted" -> MyColors.clr_ef872f_100
                                "submitted" -> MyColors.clr_00BCF1_100
                                "approved" -> MyColors.clr_36C10C_100
                                "rejected" -> MyColors.clr_CF0000_100
                                else -> MyColors.clr_36C10C_100
                            },
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontRegular
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_next_arrow),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_D3DDE7_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {


                            when(registrationDetailsData?.pan_completed_status) {
                                "not_submitted","rejected" -> {
                                    if (registrationDetailsData?.profile_completed_status=="submitted" &&
                                        registrationDetailsData?.RCBook_completed_status=="submitted" &&
                                        registrationDetailsData?.licence_completed_status=="submitted" &&
                                        registrationDetailsData?.aadhar_completed_status=="submitted") {
                                        navHostController.currentBackStackEntry?.savedStateHandle?.set("type","pan")
                                        navHostController.navigate(Routes.RCBookDetailsScreen.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column{

                        TextView(
                            text = stringResource(R.string.pan_details),
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )

                        Spacer(
                            modifier = Modifier
                                .height(6.dp)
                        )

                        TextView(
                            text = when(registrationDetailsData?.pan_completed_status){
                                "not_submitted" -> "Not Submitted"
                                "submitted" -> "Submitted"
                                "approved" -> "Approved"
                                "rejected" -> "Rejected"
                                else -> ""
                            },
                            textColor = when(registrationDetailsData?.pan_completed_status){
                                "not_submitted" -> MyColors.clr_ef872f_100
                                "submitted" -> MyColors.clr_00BCF1_100
                                "approved" -> MyColors.clr_36C10C_100
                                "rejected" -> MyColors.clr_CF0000_100
                                else -> MyColors.clr_36C10C_100
                            },
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontRegular
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_next_arrow),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_D3DDE7_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {


                            when(registrationDetailsData?.police_verification_status) {
                                "not_submitted","rejected" -> {
                                    if (registrationDetailsData?.profile_completed_status=="submitted" &&
                                        registrationDetailsData?.RCBook_completed_status=="submitted" &&
                                        registrationDetailsData?.licence_completed_status=="submitted" &&
                                        registrationDetailsData?.aadhar_completed_status=="submitted" &&
                                        registrationDetailsData?.pan_completed_status=="submitted") {
                                        navHostController.currentBackStackEntry?.savedStateHandle?.set("type","police_verification")
                                        navHostController.navigate(Routes.RCBookDetailsScreen.route) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                                else -> {}
                            }

                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                )   {
                    Column{

                        TextView(
                            text = stringResource(R.string.police_varification),
                            textColor = MyColors.clr_364B63_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontSemiBold
                        )

                        Spacer(
                            modifier = Modifier
                                .height(6.dp)
                        )

                        TextView(
                            text = when(registrationDetailsData?.police_verification_status){
                                "not_submitted" -> "Not Submitted"
                                "submitted" -> "Submitted"
                                "approved" -> "Approved"
                                "rejected" -> "Rejected"
                                else -> ""
                            },
                            textColor = when(registrationDetailsData?.police_verification_status){
                                "not_submitted" -> MyColors.clr_ef872f_100
                                "submitted" -> MyColors.clr_00BCF1_100
                                "approved" -> MyColors.clr_36C10C_100
                                "rejected" -> MyColors.clr_CF0000_100
                                else -> MyColors.clr_36C10C_100
                            },
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontRegular
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_next_arrow),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                )
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