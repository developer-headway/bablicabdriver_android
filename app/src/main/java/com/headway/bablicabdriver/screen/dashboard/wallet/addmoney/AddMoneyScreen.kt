package com.headway.bablicabdriver.screen.dashboard.wallet.addmoney

import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.dashboard.wallet.AddMoneyRequest
import com.headway.bablicabdriver.model.dashboard.wallet.CreateOrderIdRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.viewmodel.dashboard.wallet.AddMoneyVm
import com.headway.bablicabdriver.viewmodel.dashboard.wallet.CreateOrderIdVm
import com.razorpay.Checkout
import org.json.JSONObject


@Composable
fun AddMoneyScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    var walletAmount by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selAmount by rememberSaveable {
        mutableStateOf("500")
    }


    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    fun startRazorpayPayment(orderId: String) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_5Xa21NVyCh1Kil") // Replace with your Razorpay key

        try {
            val options = JSONObject()
            options.put("name", "Babli Cab")
            options.put("description", "Test Payment")
            options.put("order_id", orderId)
            options.put("currency", "INR")
            options.put("amount", "${selAmount.ifEmpty { "0" }.toInt()*100}") // amount in paise

            val prefill = JSONObject()
            prefill.put("email", sharedPreferenceManager.getEmail())
            prefill.put("contact", sharedPreferenceManager.getMobile())
            options.put("prefill", prefill)

            Log.d("msg","options: ${sharedPreferenceManager.getMobile()}")
            Log.d("msg","options: $options")
            checkout.open(activity, options)


        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val createOrderIdVm : CreateOrderIdVm = viewModel()

    fun callCreateOrderIdApi() {
        if (AppUtils.isInternetAvailable(context)) {

            val request = CreateOrderIdRequest(
                amount = "${selAmount.ifEmpty { "0" }.toInt() * 100}",
                currency = "INR",
                receipt = "rct_",
            )
            val key = "rzp_test_5Xa21NVyCh1Kil"
            val secret = "ShAhR3oMW0KDPAEZCnqCrJoe"
            val loginString = "$key:$secret"
            val base64Auth = Base64.encodeToString(loginString.toByteArray(), Base64.NO_WRAP)

            createOrderIdVm.callCreateOrderIdApi(
                token = base64Auth,
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    Log.d("msg","$response")
                    if (response?.status == "created") {
                        startRazorpayPayment(response.id)
                    } else {
//                        errorStates.bottomToastText.value = response?.message?:""
                        errorStates.bottomToastText.value = "Error"
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

    var networkAddMoneyError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    val addMoneyVm : AddMoneyVm = viewModel()
    var transactionId by rememberSaveable {
        mutableStateOf("")
    }

    fun callAddMoneyApi() {
        if (AppUtils.isInternetAvailable(context)) {

            val request = AddMoneyRequest(
                amount = selAmount,
                transaction_id = transactionId
            )
            addMoneyVm.callAddMoneyApi(
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

                        navHostController.previousBackStackEntry?.savedStateHandle?.set("refresh",true)
                        navHostController.popBackStack()
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            networkAddMoneyError = NetWorkFail.NetworkError.ordinal
        }
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    val isFirstTime by rememberSaveable{
        mutableStateOf(true)
    }
    LaunchedEffect(
        true
    ) {
        if (isFirstTime) {
            mainViewModel.paymentSuccess.value = null
        }
    }

    val paymentSuccess by mainViewModel.paymentSuccess.collectAsState()
    LaunchedEffect(paymentSuccess) {
        Log.d("msg", "mainViewModel: ${mainViewModel.paymentSuccess.value}")
        if (!mainViewModel.paymentSuccess.value.isNullOrEmpty()) {
            transactionId = paymentSuccess?:""
            callAddMoneyApi()
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
                    title = stringResource(R.string.add_money),
                    onBackPress = {
                        navHostController.popBackStack()
                    }
                )
                Image(
                    painter = painterResource(R.drawable.ic_help),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(20.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {

                        }
                )


            }

        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var imgLoading by remember {
                        mutableStateOf(true)
                    }
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data(
                                data = if (sharedPreferenceManager.getProfilePhoto().isNullOrEmpty()) {
                                    R.drawable.ic_placeholder
                                } else {
                                    sharedPreferenceManager.getProfilePhoto()
                                }
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        modifier = Modifier
                            .size(46.dp)
                            .clip(shape = CircleShape)
                            .shimmerEffect(imgLoading)
                            .clickable {

                            },
                        contentScale = ContentScale.Crop,
                        onLoading = {
                            imgLoading = true
                        },
                        onSuccess = {
                            imgLoading = false
                        }
                    )

                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        TextView(
                            text = "Babli Wallet",
                            textColor = MyColors.clr_132234_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 14.sp,
                            modifier = Modifier
                        )
                        Spacer(
                            modifier = Modifier
                                .height(4.dp)
                        )
                        TextView(
                            text = "Available Balance: ₹$walletAmount",
                            textColor = MyColors.clr_607080_100,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 12.sp,
                            modifier = Modifier

                        )
                    }


                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                TextView(
                    text = "₹$selAmount",
                    textColor = MyColors.clr_364B63_100,
                    fontFamily = MyFonts.fontSemiBold,
                    fontSize = 28.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )

                BasicTextField(
                    value = selAmount,
                    onValueChange = {it->
                        selAmount = it
                    }
                )

                Spacer(
                    modifier = Modifier
                        .height(6.dp)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )


                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    maxItemsInEachRow = 4,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("+ 200","+ 500","+ 700","+ 1000")
                        .forEach { item->
                            Box (
                                modifier = Modifier

                                    .height(30.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MyColors.clr_D3DDE7_100,
                                        shape = RoundedCornerShape(23.dp)
                                    )
                                    .clickable {
                                        selAmount = item.replace("+ ", "")
                                    }
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextView(
                                    text = item,
                                    textColor = MyColors.clr_364B63_100,
                                    fontSize = 12.sp,
                                    fontFamily = MyFonts.fontRegular
                                )
                            }

                        }
                }

                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )

                FilledButtonGradient(
                    text = stringResource(R.string.add_money),
                    textColor = MyColors.clr_white_100,
                    buttonHeight = 44.dp,
                    onClick = {
//                        https://api.razorpay.com/v1/orders
                        callCreateOrderIdApi()
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                )
                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                )

            }



        }
    }

    if (addMoneyVm._isLoading.collectAsState().value) {
        Loader()
    }
    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkAddMoneyError== NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkAddMoneyError = NetWorkFail.NoError.ordinal
                callAddMoneyApi()
            }
        },
    )




}

