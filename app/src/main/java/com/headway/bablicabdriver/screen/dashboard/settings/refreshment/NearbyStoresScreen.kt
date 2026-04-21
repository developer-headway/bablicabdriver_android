package com.headway.bablicabdriver.screen.dashboard.settings.refreshment

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.headway.bablicabdriver.model.dashboard.settings.refreshment.StoreData
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.dashboard.settings.NearbyStoresVm
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun NearbyStoresScreen(navHostController: NavHostController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)

    val vm: NearbyStoresVm = viewModel()
    val stores by vm.stores.collectAsState()
    val isLoading by vm._isLoading.collectAsState()

    val errorStates by remember { mutableStateOf(ErrorsData()) }
    var networkError by rememberSaveable { mutableIntStateOf(NetWorkFail.NoError.ordinal) }
    var isRefreshing by remember { mutableStateOf(false) }

    // Driver's current location - replace with real GPS when available
    // Using same coords as API example for now
    val driverLat = 23.0249169
    val driverLon = 72.5472575

    fun loadStores() {
        if (AppUtils.isInternetAvailable(context)) {
            vm.callNearbyStoresApi(
                token = sharedPreferenceManager.getToken(),
                lat = driverLat,
                lon = driverLon,
                radius = 100,
                errorStates = errorStates,
                onError = {
                    isRefreshing = false
                    errorStates.bottomToastText.value = it ?: ""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                }
            )
        } else {
            networkError = NetWorkFail.NetworkError.ordinal
            errorStates.showInternetError.value = true
        }
    }

    LaunchedEffect(true) { loadStores() }

    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        scope.launch {
            isRefreshing = true
            delay(300)
            loadStores()
//            isRefreshing = false
        }
    })

    fun openGoogleMaps(lat: Double, lng: Double, name: String) {
        val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(name)})")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply { setPackage("com.google.android.apps.maps") }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=$lat,$lng")))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_F7F7F7_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar  (
                title = "Nearby Stores",
                onBackPress = { navHostController.popBackStack() }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pullRefresh(refreshState)
        ) {
            if (!stores.isNullOrEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(stores ?: emptyList()) { store ->
                        StoreItemCard(
                            store = store,
                            onCallClick = {
                                val phone = store.mobile ?: return@StoreItemCard
                                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
                            },
                            onDirectionClick = {
                                val lat = store.latitude ?: return@StoreItemCard
                                val lng = store.longitude ?: return@StoreItemCard
                                openGoogleMaps(lat, lng, store.store_name ?: "Store")
                            }
                        )
                    }
                }
            } else if (!isLoading) {
                // Empty state
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_no_data),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextView(
                        text = "No nearby stores found",
                        textColor = MyColors.clr_7E7E7E_100,
                        fontFamily = MyFonts.fontMedium,
                        fontSize = 14.sp,
                        modifier = Modifier
                    )
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState
            )
        }
    }

    if (isLoading) Loader()

    CommonErrorDialogs(
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError == NetWorkFail.NetworkError.ordinal) {
                networkError = NetWorkFail.NoError.ordinal
                errorStates.showInternetError.value = false
                loadStores()
            }
        }
    )
}

@Composable
private fun StoreItemCard(
    store: StoreData,
    onCallClick: () -> Unit,
    onDirectionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neu(
                shape = Flat(RoundedCorner(14.dp)),
                lightShadowColor = MyColors.clr_7E7E7E_13,
                darkShadowColor = MyColors.clr_7E7E7E_13,
                shadowElevation = 2.dp
            )
            .clip(RoundedCornerShape(14.dp))
            .background(MyColors.clr_white_100)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Store icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MyColors.clr_00BCF1_20, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = MyColors.clr_00BCF1_100,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name + address + phone
            Column(modifier = Modifier.weight(1f)) {
                TextView(
                    text = store.store_name ?: "",
                    textColor = MyColors.clr_132234_100,
                    fontFamily = MyFonts.fontSemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                )
                if (!store.owner_name.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    TextView(
                        text = store.owner_name,
                        textColor = MyColors.clr_607080_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                TextView(
                    text = store.address ?: "",
                    textColor = MyColors.clr_607080_100,
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 12.sp,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Phone
                Row(
                    modifier = Modifier.clickable { onCallClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_call),
                        contentDescription = "Call",
                        modifier = Modifier.size(14.dp),
                        colorFilter = ColorFilter.tint(MyColors.clr_00BCF1_100)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    TextView(
                        text = store.mobile ?: "",
                        textColor = MyColors.clr_00BCF1_100,
                        fontFamily = MyFonts.fontMedium,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Direction button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MyColors.clr_36C10C_10, RoundedCornerShape(12.dp))
                    .clickable { onDirectionClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_gps),
                    contentDescription = "Direction",
                    modifier = Modifier.size(22.dp),
                    colorFilter = ColorFilter.tint(MyColors.clr_36C10C_100)
                )
            }
        }
    }
}
