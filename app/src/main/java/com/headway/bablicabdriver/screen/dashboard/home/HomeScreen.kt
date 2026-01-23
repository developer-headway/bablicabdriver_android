package com.headway.bablicabdriver.screen.dashboard.home

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Oval
import com.gandiva.neumorphic.shape.RoundedCorner
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.home.AcceptRideRequest
import com.headway.bablicabdriver.model.dashboard.home.ArrivedPickupRequest
import com.headway.bablicabdriver.model.dashboard.home.CompleteRideRequest
import com.headway.bablicabdriver.model.dashboard.home.CurrentShuttleRidePassenger
import com.headway.bablicabdriver.model.dashboard.home.RideRequests
import com.headway.bablicabdriver.model.dashboard.home.SetOnlineStatusRequest
import com.headway.bablicabdriver.model.dashboard.home.SetShuttleRouteRequest
import com.headway.bablicabdriver.model.dashboard.home.StartRideRequest
import com.headway.bablicabdriver.model.dashboard.home.UpdateDriverLocationRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.buttons.CustomSwitchButton1
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.dialog.FinishRideDialog
import com.headway.bablicabdriver.res.components.dialog.goToSettingsDialog
import com.headway.bablicabdriver.res.components.dialog.permissionDeniedDialog
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.toast.ToastExpandHorizontal
import com.headway.bablicabdriver.res.location.GPSLocationClient
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.screen.dashboard.home.chooseservicedialog.ChooseServiceDialog
import com.headway.bablicabdriver.screen.dashboard.home.openRides.RideRequestsDialog
import com.headway.bablicabdriver.screen.dashboard.home.rideDialogs.CurrentShuttleCustomersDialog
import com.headway.bablicabdriver.screen.dashboard.home.rideDialogs.DestinationDialog
import com.headway.bablicabdriver.screen.dashboard.home.rideDialogs.PickupDialog
import com.headway.bablicabdriver.screen.dashboard.home.rideDialogs.StartRideDialog
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.permissionhandler.MultiplePermissionsState
import com.headway.bablicabdriver.utils.permissionhandler.goToSettings
import com.headway.bablicabdriver.utils.permissionhandler.rememberPermissionsState
import com.headway.bablicabdriver.utils.sendNotification
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.viewmodel.dashboard.home.AcceptRideVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.ArrivedPickupVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.CancelRideVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.CompleteRideVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.HomePageVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.SetOnlineStatusVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.SetShuttleRouteVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.StartRideVm
import com.headway.bablicabdriver.viewmodel.dashboard.home.UpdateDriverLocationVm
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
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    var showServiceDialog by remember { mutableStateOf(false) }

    var currentLocation = remember {
        mutableStateOf<Location?>(null)
    }

    var finishRideDialog by rememberSaveable {
        mutableStateOf(false)
    }


    var check by rememberSaveable {
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

    val homePageVm : HomePageVm = viewModel()
    val currentRide by homePageVm.currentRide.collectAsState()
    val rideRequestList by homePageVm.rideRequestList.collectAsState()
    val homePageData by homePageVm.homePageData.collectAsState()

    fun callHomePageApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            homePageVm.callHomePageApi(
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
                        val data = response.data
                        check = data.is_online

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
    var setOnlineNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val setOnlineStatusVm : SetOnlineStatusVm = viewModel()

    fun callSetOnlineStatusApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            val request = SetOnlineStatusRequest (
                is_online = check
            )
            setOnlineStatusVm.callSetOnlineStatusApi(
                token = token,
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
                        val data = response.data
                        check = data.is_online
                        sendNotification(context = context, title = "BabliCab Driver", mess =response.message)
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            setOnlineNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    var updateLocationNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val updateDriverLocationVm : UpdateDriverLocationVm = viewModel()

    fun callUpdateDriverLocationApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            Log.d("msg","latitude: ${currentLocation.value?.latitude}")
            Log.d("msg","longitude: ${currentLocation.value?.longitude}")
            val request = UpdateDriverLocationRequest (
                latitude = "${currentLocation.value?.latitude ?: 0.0}",
                longitude = "${currentLocation.value?.longitude ?: 0.0}"
            )
            updateDriverLocationVm.callUpdateDriverLocationApi(
                token = token,
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

                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            updateLocationNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    var acceptRideNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val acceptRideVm : AcceptRideVm = viewModel()

    var acceptedRideId by rememberSaveable {
        mutableStateOf("")
    }
    fun callAcceptRideApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            Log.d("msg","latitude: ${currentLocation.value?.latitude}")
            Log.d("msg","longitude: ${currentLocation.value?.longitude}")
            val request = AcceptRideRequest (
                ride_id = acceptedRideId
            )
            acceptRideVm.callAcceptRideApi(
                token = token,
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
                        callHomePageApi()
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            acceptRideNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    var arrivedPickupNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val arrivedPickupVm : ArrivedPickupVm = viewModel()

    fun callArrivedPickupApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            Log.d("msg","latitude: ${currentLocation.value?.latitude}")
            Log.d("msg","longitude: ${currentLocation.value?.longitude}")
            val request = ArrivedPickupRequest (
                ride_id = currentRide?.ride_id?:""
            )
            arrivedPickupVm.callArrivedPickupApi(
                token = token,
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
                        callHomePageApi()
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            arrivedPickupNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    var startRideNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val startRideVm : StartRideVm = viewModel()

    var otp by remember {
        mutableStateOf("")
    }
    fun callStartRideApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            Log.d("msg","latitude: ${currentLocation.value?.latitude}")
            Log.d("msg","longitude: ${currentLocation.value?.longitude}")
            val request = StartRideRequest (
                ride_id = currentRide?.ride_id?:"",
                otp = otp
            )
            startRideVm.callStartRideApi(
                token = token,
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
                        callHomePageApi()
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            startRideNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    var completeRideNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val completeRideVm : CompleteRideVm = viewModel()

    var selRideId by rememberSaveable {
        mutableStateOf("")
    }
    fun callCompleteRideApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            Log.d("msg","latitude: ${currentLocation.value?.latitude}")
            Log.d("msg","longitude: ${currentLocation.value?.longitude}")
            val request = CompleteRideRequest (
                ride_id = selRideId
            )
            completeRideVm.callCompleteRideApi(
                token = token,
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

                        callHomePageApi()
                        if (currentRide?.ride_type == "one_way") {
                            navHostController.currentBackStackEntry?.savedStateHandle?.set("price",currentRide?.total_price.toString())
                            navHostController.currentBackStackEntry?.savedStateHandle?.set("ride_id",currentRide?.ride_id)
                            navHostController.navigate(Routes.PaymentSuccessScreen.route) {
                                launchSingleTop = true
                            }
                        }
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            completeRideNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////


    var cancelRideNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val cancelRideVm : CancelRideVm = viewModel()
    fun callCancelRideApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            Log.d("msg","latitude: ${currentLocation.value?.latitude}")
            Log.d("msg","longitude: ${currentLocation.value?.longitude}")
            val request = CompleteRideRequest (
                ride_id = acceptedRideId
            )
            cancelRideVm.callCancelRideApi(
                token = token,
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
                        callHomePageApi()
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            cancelRideNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    var setShuttleNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }

    val setShuttleRouteVm : SetShuttleRouteVm = viewModel()

    fun callSetShuttleRouteApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val token = sharedPreferenceManager.getToken()
            val userId = sharedPreferenceManager.getUserId()
            val request = SetShuttleRouteRequest(
                user_id = userId,
                ride_type = "one_way",
                route_id = ""
            )
            setShuttleRouteVm.callSetShuttleRouteApi(
                token = token,
                request = request,
                errorStates = errorStates,
                onError = {
                    isRefreshing = false
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    isRefreshing = false
                    if (response?.status == false) {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    } else {
                        callHomePageApi()
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            setShuttleNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////



    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            callHomePageApi()
        }
    })

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState {}
    val coroutineScope = rememberCoroutineScope()
    var locationPermissions : MultiplePermissionsState? = null

    fun callLocationPermission() {
        locationPermissions?.launchPermissionRequestsAndAction()
    }
    locationPermissions = rememberPermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        onGrantedAction = {
            val gpsLocationClient = GPSLocationClient()
            gpsLocationClient.getLocationUpdates(context = context, mainViewModel = mainViewModel)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->

                location?.let {
                    currentLocation.value = location
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(
                                LatLng(it.latitude, it.longitude),
                                16f // zoom level
                            ),
                            durationMs = 10
                        )
                    }
                    callUpdateDriverLocationApi()
                }
            }
        },
        onDeniedAction = {
            permissionDeniedDialog(context) {
//                context.goToSettings(it)
                callLocationPermission()
            }
        },
        onPermanentlyDeniedAction = {
            goToSettingsDialog(context) {
                context.goToSettings(it)
            }
        }
    )

    var isFirstTime by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(true) {
        Log.d("msg","isFirstTime: $isFirstTime")
        val isRefresh = navHostController.currentBackStackEntry?.savedStateHandle?.get<Boolean?>("refresh")?:false
        if (isFirstTime || isRefresh) {
            navHostController.currentBackStackEntry?.savedStateHandle?.set("refresh",false)
            isFirstTime = false

            callLocationPermission()
            callHomePageApi()
        }
    }
    //////////////////
    //////////////////
    DisposableEffect(true) {
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("msg","received")
                val rideRequests = intent.getSerializableExtra("ride_request", RideRequests::class.java)
                Log.d("msg","received: $rideRequests")
                rideRequests?.let {
                    homePageVm.updateRideRequestList(rideRequests)
                }
            }
        }
        val intentFilter = IntentFilter("com.notification.ride_request")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        }
        onDispose {
            context.unregisterReceiver(broadcastReceiver)
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
                .fillMaxSize()
        ) {

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
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            // Map setup
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                properties = MapProperties(
                                    isMyLocationEnabled = locationPermissions.allPermissionsGranted,
                                    mapType = MapType.NORMAL,
                                ),
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = false,  // hide plus/minus
                                    compassEnabled = false,       // hide compass
                                    myLocationButtonEnabled = false, // hide default location button
                                    mapToolbarEnabled = false,    // hide navigation toolbar
                                    rotationGesturesEnabled = true,
                                    tiltGesturesEnabled = false
                                )
                            )

                        }


                        AvailableForRideBox(
                            check = check,
                            onChangeClick = {
                                showServiceDialog = true
                            },
                            onAvailabilityChange = {checked->
                                if (!checked) {
                                    check = false
                                    callSetOnlineStatusApi()
                                } else {
                                    showServiceDialog = true
                                }
                            },
                            homePageVm = homePageVm
                        )

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
                                )
                                .clickable {
                                    if (locationPermissions.allPermissionsGranted) {
                                        coroutineScope.launch {
                                            cameraPositionState.animate(
                                                update = CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(
                                                        mainViewModel.currentLocation.value?.latitude
                                                            ?: 0.0,
                                                        mainViewModel.currentLocation.value?.longitude
                                                            ?: 0.0
                                                    ),
                                                    16f // zoom level
                                                ),
                                                durationMs = 500
                                            )
                                        }
                                    } else {
                                        locationPermissions.launchPermissionRequestsAndAction()
                                    }

                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_location_recenter),
                                modifier = Modifier
                                    .size(22.dp),
                                contentDescription = stringResource(R.string.img_des)
                            )
                        }


                        when(currentRide?.ride_status) {
                            "accepted" -> {
                                PickupDialog(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .clickable(
                                            interactionSource = null,
                                            enabled = false
                                        ) {},
                                    currentRide = currentRide,
                                    onArrivedClick = {
                                        callArrivedPickupApi()
                                    }
                                )
                            }
                            "arrived" -> {
                                StartRideDialog(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .clickable(
                                            interactionSource = null,
                                            enabled = false
                                        ) {},
                                    vehicleIdealNumber = homePageData?.vehicle_ideal_number
                                )
                            }
                            "started" -> {
                                DestinationDialog(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .clickable(
                                            interactionSource = null,
                                            enabled = false
                                        ) {},
                                    onCompleteRideClick = {
                                        selRideId = currentRide?.ride_id?:""
                                        callCompleteRideApi()
                                    },
                                    currentRide = currentRide
                                )
                            }
                        }

                        if(!homePageData?.Current_shuttle_ride_passengers.isNullOrEmpty()) {
                            Log.d("msg","homePageData: ${homePageData?.Current_shuttle_ride_passengers}")
                            CurrentShuttleCustomersDialog(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .clickable(
                                        interactionSource = null,
                                        enabled = false
                                    ) {

                                    },
                                list = homePageData?.Current_shuttle_ride_passengers,
                                finishRideDialog = {
                                    selRideId = it?:""
                                    finishRideDialog = true
                                }
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

        }



        CommonErrorDialogs(
            showToast = false,
            errorStates = errorStates,
            onNoInternetRetry = {
                if (networkError== NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    networkError = NetWorkFail.NoError.ordinal
                    callHomePageApi()
                }
                if (setOnlineNetworkError== NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    setOnlineNetworkError = NetWorkFail.NoError.ordinal
                    callSetOnlineStatusApi()
                }

                if (updateLocationNetworkError== NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    updateLocationNetworkError = NetWorkFail.NoError.ordinal
                    callUpdateDriverLocationApi()
                }
                if (acceptRideNetworkError == NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    acceptRideNetworkError = NetWorkFail.NoError.ordinal
                    callAcceptRideApi()
                }

                if (arrivedPickupNetworkError == NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    arrivedPickupNetworkError = NetWorkFail.NoError.ordinal
                    callArrivedPickupApi()
                }

                if (startRideNetworkError == NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    startRideNetworkError = NetWorkFail.NoError.ordinal
                    callStartRideApi()
                }

                if (completeRideNetworkError == NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    completeRideNetworkError = NetWorkFail.NoError.ordinal
                    callCompleteRideApi()
                }

                if (cancelRideNetworkError == NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    cancelRideNetworkError = NetWorkFail.NoError.ordinal
                    callCancelRideApi()
                }
                if (setShuttleNetworkError== NetWorkFail.NetworkError.ordinal) {
                    errorStates.showInternetError.value = false
                    setShuttleNetworkError = NetWorkFail.NoError.ordinal
                    callSetShuttleRouteApi()
                }

            },
        )


        if (showServiceDialog) {
            ChooseServiceDialog(
                onDismiss = { showServiceDialog = false },
                onOneWaySelected = {
                    showServiceDialog = false
                    //set route type one way
                    callSetShuttleRouteApi()
                },
                onShuttleSelected = {
                    showServiceDialog = false
//                    onChangeClick()
                    navHostController.navigate(Routes.SetRouteScreen.route) {
                        launchSingleTop = true
                    }
                }
            )
        }



        if (homePageVm._isLoading.collectAsState().value
            || setOnlineStatusVm._isLoading.collectAsState().value
            || acceptRideVm._isLoading.collectAsState().value
            || setShuttleRouteVm._isLoading.collectAsState().value) {
            Loader()
        }

        if (!rideRequestList.isNullOrEmpty()) {
            Dialog(
                onDismissRequest = {

                },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false // 👈 makes it full screen
                )
            ) {
                RideRequestsDialog(
                    homePageVm = homePageVm,
                    onAcceptClick = { it ->
                        acceptedRideId = it?:""
                        callAcceptRideApi()
                    },
                    onCancelClick = {
                        acceptedRideId = it?:""
                        callCancelRideApi()
                    }
                )
            }
        }


        // Show dialog when state is true
        if (finishRideDialog) {
            FinishRideDialog (
                onDismiss = { finishRideDialog = false },
                onCancelRide = {
                    // Handle cancel ride action
                    finishRideDialog = false
                    callCompleteRideApi()
                },
                onGoBack = {
                    // Handle go back action
                    finishRideDialog = false
                }
            )
        }



    }


}


@Composable
fun AvailableForRideBox(
    check: Boolean = true,
    onAvailabilityChange: (Boolean) -> Unit = {},
    homePageVm: HomePageVm,
    onChangeClick: () -> Unit = {}
) {
    val homePageData by homePageVm.homePageData.collectAsState()
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip( shape = RoundedCornerShape(16.dp))
            .background(
                color = MyColors.clr_white_100,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MyColors.clr_00BCF1_100,
                shape = RoundedCornerShape(16.dp)
            )

    ) {
        // Available for Ride header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                text = stringResource(R.string.available_for_ride),
                textColor = MyColors.clr_132234_100,
                fontFamily = MyFonts.fontSemiBold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                CustomSwitchButton1(
                    value = check,
                    onCheckedChange = { checked ->
                        onAvailabilityChange(checked)
                    }
                )
            }


            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.ic_notification),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .size(18.dp),
                tint = MyColors.clr_132234_100,
            )
        }

        if (check) {
            Box(
                modifier = Modifier

                    .fillMaxWidth()
                    .background(
                        color = MyColors.clr_00BCF1_100
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        TextView(
                            text = stringResource(if (homePageData?.current_ride_type?.ride_type=="one_way")R.string.one_way else R.string.shuttle),
                            textColor = MyColors.clr_white_100,
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 14.sp
                        )

                        if (homePageData?.current_ride_type?.ride_type=="shuttle") {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextView(
                                    text = "${homePageData?.current_ride_type?.shuttle_route_start_selected_address} → ${homePageData?.current_ride_type?.shuttle_route_end_selected_address}",
                                    textColor = MyColors.clr_white_100,
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MyColors.clr_white_100,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onChangeClick()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        TextView(
                            text = stringResource(R.string.change),
                            textColor = MyColors.clr_00BCF1_100,
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }


    }
}













