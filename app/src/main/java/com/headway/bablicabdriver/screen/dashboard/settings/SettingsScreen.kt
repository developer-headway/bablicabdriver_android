package com.headway.bablicabdriver.screen.dashboard.settings

import android.provider.Settings
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.res.components.dialog.ConfirmDeleteAccountDialog
import com.headway.bablicabdriver.res.components.dialog.ConfirmLogOutDialog
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.commondataclass.DeviceUIdRequest
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.viewmodel.settings.DeleteAccountVm
import com.headway.bablicabdriver.viewmodel.settings.LogoutVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.forEachIndexed
import kotlin.collections.lastIndex

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    errorStatesDashboard: ErrorsData
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)

    var mobileNum by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var profilePic by remember {
        mutableStateOf("")
    }
    var isRefreshing by remember {
        mutableStateOf(false)
    }


    val showConfirmLogoutDialog = remember {
        mutableStateOf(false)
    }
    val showDeleteAccountDialog = rememberSaveable {
        mutableStateOf(false)
    }


    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }

    val logoutVm : LogoutVm = viewModel()
    fun callLogoutApi() {

        if (AppUtils.isInternetAvailable(context)) {

            val uniqueId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val request = DeviceUIdRequest(
                device_uid = uniqueId
            )
            logoutVm.callLogoutApi(
                token = sharedPreferenceManager.getToken(),
                request = request,
                errorStates = errorStates,
                onSuccess = {
                    AppUtils.logoutAndClearData(context)
                },
                onError = {
                    AppUtils.logoutAndClearData(context)
                }
            )
        } else {
            AppUtils.logoutAndClearData(context)
        }

    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val deleteAccountVm : DeleteAccountVm = viewModel()
    fun callDeleteAccountApi() {
        if (AppUtils.isInternetAvailable(context)) {
            deleteAccountVm.callDeleteAccountApi(
                token = sharedPreferenceManager.getToken(),
                errorStates = errorStates,
                onSuccess = {
                    AppUtils.logoutAndClearData(context)
                },
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                }
            )
        } else {
            networkError = NetWorkFail.NetworkError.ordinal
            errorStates.showInternetError.value = true
        }

    }


    LaunchedEffect(
        true
    ) {
        profilePic = sharedPreferenceManager.getProfilePhoto()
        mobileNum = sharedPreferenceManager.getMobile()
        name = "${sharedPreferenceManager.getFirstName()} ${sharedPreferenceManager.getLastName()}"
    }


    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            isRefreshing = false
        }
    })

    val menu1Item = listOf(
        Pair(R.drawable.ic_my_rides, R.string.my_vehicle_details),
        Pair(R.drawable.ic_doc, R.string.document_info),
        Pair(R.drawable.ic_bank, R.string.bank_details),
        Pair(R.drawable.ic_safety, R.string.safety),
    )

    val menu2Item = listOf(
        Pair(R.drawable.ic_help_outline, R.string.help),
        Pair(R.drawable.ic_term_condition, R.string.terms_conditions),
        Pair(R.drawable.ic_about, R.string.about),
    )


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                showBackIcon = false,
                title = stringResource(R.string.rewards)
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
                    .verticalScroll(rememberScrollState())
            ) {


                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .neu(
                            shape = Flat(RoundedCorner(16.dp)),
                            lightShadowColor = MyColors.clr_7E7E7E_13,
                            darkShadowColor = MyColors.clr_7E7E7E_13,
                            shadowElevation = 2.dp
                        )
                        .background(
                            color = MyColors.clr_white_100,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                                "is_edit",
                                true
                            )
                            navHostController.navigate(Routes.ProfileScreen.route) {
                                launchSingleTop = true
                            }
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .padding(2.dp)
                    ) {

                        var imgLoading by remember {
                            mutableStateOf(true)
                        }
                        AsyncImage(
                            model = ImageRequest
                                .Builder(context)
                                .data(
                                    profilePic.ifEmpty {
                                        R.drawable.ic_placeholder
                                    }
                                )
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = CircleShape)
                                .shimmerEffect(imgLoading),
                            contentScale = ContentScale.Crop,
                            onLoading = {
                                imgLoading = true
                            },
                            onSuccess = {
                                imgLoading = false
                            }
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .width(8.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {

                        TextView(
                            text = name,
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 16.sp,
                            modifier = Modifier
                        )
                        Spacer(
                            modifier = Modifier
                                .width(4.dp)
                        )
                        TextView(
                            text = "+91 $mobileNum",
                            textColor = MyColors.clr_364B63_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 12.sp,
                            modifier = Modifier
                        )

                    }

                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_next_arrow),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(18.dp)
                    )

                }


                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )



                Column (
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .neu(
                            shape = Flat(RoundedCorner(16.dp)),
                            lightShadowColor = MyColors.clr_7E7E7E_13,
                            darkShadowColor = MyColors.clr_7E7E7E_13,
                            shadowElevation = 2.dp
                        )
                        .background(
                            color = MyColors.clr_white_100,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(top = 6.dp)
                ) {
                    menu1Item.forEachIndexed { index, item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when (item.second) {
                                        R.string.my_vehicle_details -> {
                                            navHostController.navigate(Routes.MyVehicleScreen.route) {
                                                launchSingleTop = true
                                            }
                                        }
                                        R.string.document_info -> {
                                            navHostController.navigate(Routes.DocumentInfoScreen.route) {
                                                launchSingleTop = true
                                            }
                                        }
                                        R.string.bank_details -> {
                                            navHostController.navigate(Routes.BankDetailsScreen.route) {
                                                launchSingleTop = true
                                            }
                                        }

                                    }
                                }
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(item.first),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .size(22.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(12.dp)
                                )
                                TextView(
                                    text = stringResource(item.second),
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .width(12.dp)
                                )
                                Image(
                                    painter = painterResource(R.drawable.ic_next_arrow),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .size(16.dp)
                                )

                            }

                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )

                            if (menu1Item.lastIndex !=index) {
                                HorizontalDivider(
                                    color = MyColors.clr_D3DDE7_100,
                                    modifier = Modifier
                                        .padding(start = 52.dp)
                                )
                            }


                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )


                Column (
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .neu(
                            shape = Flat(RoundedCorner(16.dp)),
                            lightShadowColor = MyColors.clr_7E7E7E_13,
                            darkShadowColor = MyColors.clr_7E7E7E_13,
                            shadowElevation = 2.dp
                        )
                        .background(
                            color = MyColors.clr_white_100,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(top = 6.dp)
                ) {
                    menu2Item.forEachIndexed { index, item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                }
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(item.first),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .size(22.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(12.dp)
                                )
                                TextView(
                                    text = stringResource(item.second),
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .width(12.dp)
                                )
                                Image(
                                    painter = painterResource(R.drawable.ic_next_arrow),
                                    contentDescription = stringResource(R.string.img_des),
                                    modifier = Modifier
                                        .size(16.dp)
                                )

                            }

                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )

                            if (menu2Item.lastIndex !=index) {
                                HorizontalDivider(
                                    color = MyColors.clr_D3DDE7_100,
                                    modifier = Modifier
                                        .padding(start = 52.dp)
                                )
                            }


                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )
                Column (
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .neu(
                            shape = Flat(RoundedCorner(16.dp)),
                            lightShadowColor = MyColors.clr_7E7E7E_13,
                            darkShadowColor = MyColors.clr_7E7E7E_13,
                            shadowElevation = 2.dp
                        )
                        .clip( shape = RoundedCornerShape(16.dp))
                        .background(
                            color = MyColors.clr_white_100,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .clickable {
                                showConfirmLogoutDialog.value = true
                            }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(22.dp)
                        )
                        Spacer(
                            modifier = Modifier
                                .width(12.dp)
                        )
                        TextView(
                            text = stringResource(R.string.sign_out),
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 14.sp,
                            modifier = Modifier
                        )


                    }


                    HorizontalDivider(
                        color = MyColors.clr_D3DDE7_100,
                        modifier = Modifier
                            .padding(start = 52.dp)
                    )
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .clickable {
                                showDeleteAccountDialog.value = true
                            }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_deleteaccount),
                            contentDescription = stringResource(R.string.img_des),
                            modifier = Modifier
                                .size(22.dp)
                        )
                        Spacer(
                            modifier = Modifier
                                .width(12.dp)
                        )
                        TextView(
                            text = stringResource(R.string.delete_account),
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 14.sp,
                            modifier = Modifier
                        )


                    }

                }



                Spacer(
                    modifier = Modifier
                        .height(60.dp)
                )
            }

            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = refreshState
            )

        }
    }


    if (showConfirmLogoutDialog.value) {
        ConfirmLogOutDialog(visible = showConfirmLogoutDialog) {
            callLogoutApi()
        }
    }

    if (showDeleteAccountDialog.value) {
        ConfirmDeleteAccountDialog(visible = showDeleteAccountDialog) {
            callDeleteAccountApi()
        }
    }

    CommonErrorDialogs(errorStates) {
        if (networkError == NetWorkFail.NetworkError.ordinal) {
            networkError = NetWorkFail.NoError.ordinal
            errorStates.showInternetError.value = false
            callDeleteAccountApi()
        }
    }




}