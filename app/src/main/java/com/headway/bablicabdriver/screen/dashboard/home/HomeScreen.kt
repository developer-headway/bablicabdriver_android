package com.headway.bablicabdriver.screen.dashboard.home

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Oval
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.buttons.CustomSwitchButton1
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.toast.ToastExpandHorizontal
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.dialog.RideSelectDialog
import com.headway.bablicabdriver.res.components.dialog.goToSettingsDialog
import com.headway.bablicabdriver.res.components.dialog.permissionDeniedDialog
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.permissionhandler.goToSettings
import com.headway.bablicabdriver.utils.permissionhandler.rememberPermissionsState
import com.headway.bablicabdriver.viewmodel.MainViewModel
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    errorStatesDashboard: ErrorsData
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val showRideDialog = rememberSaveable {
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


    var selectedWay by rememberSaveable {
        mutableStateOf(0)
    }


    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            isRefreshing = false
        }
    })
    val profilePhoto by remember {
        mutableStateOf(sharedPreferenceManager.getProfilePhoto())
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var currentLatLng by rememberSaveable {
        mutableStateOf<LatLng?>(null)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(23.2599, 77.4126), 15f)
    }

    val locationPermissions = rememberPermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        onGrantedAction = {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                currentLatLng = LatLng(location.latitude, location.longitude)
                // Use currentLatLng for marker/camera
                Log.d("msg","currentLatLng $currentLatLng")

                cameraPositionState.position =  CameraPosition.fromLatLngZoom(currentLatLng ?: LatLng(23.2599, 77.4126), 15f) // Initial coordinates and zoom
            }
        },
        onDeniedAction = {
            permissionDeniedDialog(context) {}
        },
        onPermanentlyDeniedAction = {
            goToSettingsDialog(context) {
                context.goToSettings(it)
            }
        }
    )

    LaunchedEffect(true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            locationPermissions.launchPermissionRequestsAndAction()
        }
    }


    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ToastExpandHorizontal(
                    showBottomToast = errorStates.showBottomToast,
                    s = errorStates.bottomToastText.value
                )
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

                Box {
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = true)
                    ) {

                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .height(52.dp)
                            .neu(
                                lightShadowColor = MyColors.clr_7E7E7E_13,
                                darkShadowColor = MyColors.clr_7E7E7E_13,
                                shape = Flat(RoundedCorner(12.dp))
                            )
                            .background(
                                color = MyColors.clr_white_100,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        TextView(
                            text = "Available for Ride ?",
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 14.sp,
                            maxLines = 1,
                            modifier = Modifier
                        )
                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                        )

                        var check by rememberSaveable {
                            mutableStateOf(false)
                        }

                        CustomSwitchButton1(
                            value = check,
                            onCheckedChange = {checked ->
                                check = checked

                                navHostController.navigate(Routes.OpenRidesScreen.route) {
                                    launchSingleTop = true
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                        )

                        Box(
                            modifier = Modifier
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_notification),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .size(16.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = MyColors.clr_00BCF1_100,
                                        shape = CircleShape
                                    )
                                    .clip(shape = CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }


                    }


                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp, bottom = 30.dp)
                            .size(40.dp)
                            .align(Alignment.BottomEnd)
                            .neu(
                                darkShadowColor = MyColors.clr_7E7E7E_13,
                                lightShadowColor = MyColors.clr_7E7E7E_13,
                                shadowElevation = 2.dp,
                                shape = Flat(Oval),
                            )
                            .background(
                                color = MyColors.clr_white_100,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_location_recenter),
                            modifier = Modifier
                                .size(22.dp),
                            contentDescription = stringResource(R.string.img_des)
                        )
                    }


                }




            }




            PullRefreshIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = refreshState
            )



        }


        CommonErrorDialogs(
            showToast = false,
            errorStates = errorStates,
            onNoInternetRetry = {
                if (networkError== NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    networkError = NetWorkFail.NoError.ordinal
                }
            },
        )



        if (false) {
            Loader()
        }

    }




}



