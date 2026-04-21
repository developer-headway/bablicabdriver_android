package com.headway.bablicabdriver.screen.dashboard.settings.refreshment

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Store
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.settings.refreshment.RefreshmentItemData
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.viewmodel.dashboard.settings.RefreshmentItemsVm

// ─── Screen ─────────────────────────────────────────────────────────────────

@Composable
fun RefreshmentScreen(navHostController: NavHostController) {

    val context = LocalContext.current
    val sharedPreferenceManager = SharedPreferenceManager(context)

    val vm: RefreshmentItemsVm = viewModel()
    val itemsData by vm.itemsData.collectAsState()
    val isLoading by vm._isLoading.collectAsState()

    val items = itemsData?.items ?: emptyList()

    val errorStates by remember { mutableStateOf(ErrorsData()) }
    var networkError by rememberSaveable { mutableIntStateOf(NetWorkFail.NoError.ordinal) }

    fun loadItems() {
        if (AppUtils.isInternetAvailable(context)) {
            vm.callRefreshmentItemsApi(
                token = sharedPreferenceManager.getToken(),
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it ?: ""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                }
            )
        } else {
            networkError = NetWorkFail.NetworkError.ordinal
            errorStates.showInternetError.value = true
        }
    }

    LaunchedEffect(true) { loadItems() }

    // Edit mode state - separate from view qty
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var editQtys by rememberSaveable { mutableStateOf(listOf<String>()) }
    var errors by rememberSaveable { mutableStateOf(listOf<String>()) }

    // Reset editQtys when items load or edit mode starts
    LaunchedEffect(items.size) {
        editQtys = List(items.size) { "0" }
        errors = List(items.size) { "" }
    }

    // Total amount from editQtys
    val totalAmount = items.mapIndexed { i, item ->
        val q = editQtys.getOrNull(i)?.toIntOrNull() ?: 0
        val p = item.price ?: 0
        q * p
    }.sum()

    val hasChanges = editQtys.any { (it.toIntOrNull() ?: 0) > 0 }

    // QR scan success - enable edit mode
    val scanSuccess = navHostController.currentBackStackEntry
        ?.savedStateHandle?.get<Boolean>("scan_success") ?: false

    LaunchedEffect(scanSuccess) {
        if (scanSuccess) {
            navHostController.currentBackStackEntry?.savedStateHandle?.set("scan_success", false)
            editQtys = List(items.size) { "0" }
            errors = List(items.size) { "" }
            isEditMode = true
        }
    }

    // Also handle legacy is_scan_qr_code key
    LaunchedEffect(true) {
        val isScan = navHostController.currentBackStackEntry
            ?.savedStateHandle?.get<Boolean?>("is_scan_qr_code") ?: false
        if (isScan) {
            navHostController.currentBackStackEntry?.savedStateHandle?.set("is_scan_qr_code", false)
            editQtys = List(items.size) { "0" }
            errors = List(items.size) { "" }
            isEditMode = true
        }
    }

    fun validate(): Boolean {
        val newErrors = editQtys.mapIndexed { i, q ->
            when {
                q.isBlank()             -> "Qty cannot be empty"
                q.toIntOrNull() == null -> "Enter a valid number"
                q.toInt() < 0           -> "Cannot be negative"
                q.toInt() > 9999        -> "Max 9999 allowed"
                else                    -> ""
            }
        }
        errors = newErrors
        return newErrors.all { it.isEmpty() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_F7F7F7_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = if (isEditMode) "Update Refreshment" else "Refreshment",
                onBackPress = { navHostController.popBackStack() }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        if (isEditMode) {
                            if (hasChanges) MyColors.clr_00BCF1_100 else MyColors.clr_7E7E7E_100
                        } else {
                            MyColors.clr_00BCF1_100
                        }
                    )
                    .clickable {
                        if (isEditMode) {
                            if (hasChanges && validate()) {
                                // TODO: API call to submit purchase
                                isEditMode = false
                                editQtys = List(items.size) { "0" }
                                errors = List(items.size) { "" }
                                Toast.makeText(context, "Purchase Successful!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            navHostController.navigate(
                                Routes.QrScannerScreen.createRoute(0)
                            ) { launchSingleTop = true }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                TextView(
                    text = if (isEditMode) {
                        if (totalAmount > 0) "Pay Rs.$totalAmount" else "Add Items to Pay"
                    } else {
                        "Scan QR Code"
                    },
                    fontSize = 14.sp,
                    fontFamily = MyFonts.fontSemiBold,
                    textColor = MyColors.clr_white_100,
                    modifier = Modifier
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // Nearby stores banner
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MyColors.clr_00BCF1_20)
                            .border(
                                width = 1.dp,
                                color = MyColors.clr_00BCF1_100.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                navHostController.navigate(Routes.NearbyStoresScreen.route) {
                                    launchSingleTop = true
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = MyColors.clr_00BCF1_100,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            TextView(
                                text = "Need to restock?",
                                textColor = MyColors.clr_132234_100,
                                fontFamily = MyFonts.fontSemiBold,
                                fontSize = 13.sp,
                                modifier = Modifier
                            )
                            TextView(
                                text = "View nearby stores",
                                textColor = MyColors.clr_00BCF1_100,
                                fontFamily = MyFonts.fontRegular,
                                fontSize = 12.sp,
                                modifier = Modifier
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Item cards from API
                itemsIndexed(items) { index, item ->
                    RefreshmentCard(
                        item = item,
                        qty = if (isEditMode) editQtys.getOrElse(index) { "0" }
                              else "${item.stock_quantity ?: 0}",
                        error = errors.getOrElse(index) { "" },
                        isEditMode = isEditMode,
                        onQtyChange = { newQty ->
                            editQtys = editQtys.toMutableList().also { it[index] = newQty }
                            errors = errors.toMutableList().also { it[index] = "" }
                        }
                    )
                }
            }
        }
    }

    if (isLoading) Loader()

    CommonErrorDialogs(
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError == NetWorkFail.NetworkError.ordinal) {
                networkError = NetWorkFail.NoError.ordinal
                errorStates.showInternetError.value = false
                loadItems()
            }
        }
    )
}

// ─── Refreshment item card ───────────────────────────────────────────────────

@Composable
private fun RefreshmentCard(
    item: RefreshmentItemData,
    qty: String,
    error: String,
    isEditMode: Boolean,
    onQtyChange: (String) -> Unit
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: dot + name + price
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (item.is_in_stock == true) MyColors.clr_00BCF1_100
                                else MyColors.clr_D3DDE7_100,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextView(
                        text = item.name ?: "",
                        textColor = MyColors.clr_132234_100,
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                TextView(
                    text = "Rs.${item.price ?: 0} / piece",
                    textColor = MyColors.clr_08875D_100,
                    fontFamily = MyFonts.fontMedium,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                if (!item.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    TextView(
                        text = item.description,
                        textColor = MyColors.clr_607080_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            // Right: qty badge (view) or stepper (edit)
            if (isEditMode) {
                QtyStepper(
                    qty = qty,
                    onQtyChange = onQtyChange,
                    hasError = error.isNotEmpty()
                )
            } else {
                Box(
                    modifier = Modifier
                        .background(MyColors.clr_F0FCFF_100, RoundedCornerShape(10.dp))
                        .border(1.dp, MyColors.clr_00BCF1_20, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TextView(
                            text = qty,
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontBold,
                            fontSize = 18.sp,
                            modifier = Modifier
                        )
                        TextView(
                            text = "qty",
                            textColor = MyColors.clr_607080_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 10.sp,
                            modifier = Modifier
                        )
                    }
                }
            }
        }

        // Error strip
        AnimatedVisibility(
            visible = error.isNotEmpty(),
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            Column {
                HorizontalDivider(color = MyColors.clr_FA4949_100.copy(alpha = 0.3f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MyColors.clr_FA4949_100.copy(alpha = 0.06f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    TextView(
                        text = "  $error",
                        textColor = MyColors.clr_FA4949_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 11.sp,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

// ─── Qty stepper ─────────────────────────────────────────────────────────────

@Composable
private fun QtyStepper(
    qty: String,
    onQtyChange: (String) -> Unit,
    hasError: Boolean
) {
    val current = qty.toIntOrNull() ?: 0

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = if (current > 0) MyColors.clr_00BCF1_100 else MyColors.clr_D3DDE7_100,
                    shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                )
                .clickable { if (current > 0) onQtyChange("${current - 1}") },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Remove, contentDescription = "Decrease",
                tint = MyColors.clr_white_100, modifier = Modifier.size(16.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(52.dp)
                .height(34.dp)
                .background(MyColors.clr_white_100)
                .border(1.dp, if (hasError) MyColors.clr_FA4949_100 else MyColors.clr_D3DDE7_100),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = qty,
                onValueChange = { v ->
                    if (v.length <= 4 && v.all { it.isDigit() }) onQtyChange(v)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontFamily = MyFonts.fontSemiBold,
                    fontSize = 14.sp,
                    color = if (hasError) MyColors.clr_FA4949_100 else MyColors.clr_132234_100,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = if (current < 9999) MyColors.clr_00BCF1_100 else MyColors.clr_D3DDE7_100,
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                )
                .clickable { if (current < 9999) onQtyChange("${current + 1}") },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add, contentDescription = "Increase",
                tint = MyColors.clr_white_100, modifier = Modifier.size(16.dp)
            )
        }
    }
}
