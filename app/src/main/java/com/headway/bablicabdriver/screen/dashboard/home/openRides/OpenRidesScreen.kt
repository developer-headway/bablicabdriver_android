package com.headway.bablicabdriver.screen.dashboard.home.openRides

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.model.dashboard.BottomTab
import com.headway.bablicabdriver.res.components.bar.ShowSnackBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.screen.dashboard.home.HomeScreen
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.collections.forEachIndexed


@Composable
fun OpenRidesScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var showSnackBar by rememberSaveable {
        mutableStateOf(false)
    }
    var snackBarText by rememberSaveable {
        mutableStateOf("")
    }

    BackHandler {
        showSnackBar = true
        snackBarText = "can not to back"
    }

    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = {
//            ShowSnackBar(
//                message = snackBarText,
//                showSb = showSnackBar,
//                openSnackBar = {
//                    mainViewModel.updateSnackBar(false)
//                }
//            )

            if (showSnackBar) {
                Snackbar(action = {}) {
                    Text(snackBarText)
                }
            }

        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier =  Modifier
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    items(
                        listOf("","")
                    ) {



                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    color = MyColors.clr_00BCF1_100
                                )
                        ) {

                            Spacer(
                                modifier = Modifier
                                    .height(18.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    modifier = Modifier
                                        .size(20.dp),
                                    contentDescription = stringResource(R.string.img_des),
                                    painter = painterResource(R.drawable.ic_one_way)
                                )
                                Text(
                                    text = "0.9 km (From Your Current) ",
                                    fontSize = 12.sp,
                                    fontFamily = MyFonts.fontSemiBold,
                                    color = MyColors.clr_364B63_100
                                )
                            }


                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(18.dp)
                                            .background(
                                                color = MyColors.clr_D3DDE7_100
                                            )
                                    )
                                    Image(
                                        modifier = Modifier
                                            .size(18.dp),
                                        painter = painterResource(R.drawable.ic_point_green),
                                        contentDescription = stringResource(R.string.img_des)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(18.dp)
                                            .background(
                                                color = MyColors.clr_D3DDE7_100
                                            )
                                    )

                                }

                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                )
                                Text(
                                    text = "Bus Sta Upas, Majestic, Bengaluru, Karnataka 560009",
                                    fontSize = 14.sp,
                                    fontFamily = MyFonts.fontRegular,
                                    color = MyColors.clr_132234_100,
                                )
                            }


                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    modifier = Modifier
                                        .size(20.dp),
                                    contentDescription = stringResource(R.string.img_des),
                                    painter = painterResource(R.drawable.ic_one_way)
                                )
                                Text(
                                    text = "15.36 km",
                                    fontSize = 12.sp,
                                    fontFamily = MyFonts.fontSemiBold,
                                    color = MyColors.clr_364B63_100
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(18.dp)
                                            .background(
                                                color = MyColors.clr_D3DDE7_100
                                            )
                                    )
                                    Image(
                                        modifier = Modifier
                                            .size(18.dp),
                                        painter = painterResource(R.drawable.ic_point_red),
                                        contentDescription = stringResource(R.string.img_des)
                                    )

                                }

                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                )
                                Text(
                                    text = "M.G. Railway Colony, Majestic, Bengaluru, Karnataka 560023",
                                    fontSize = 14.sp,
                                    fontFamily = MyFonts.fontRegular,
                                    color = MyColors.clr_132234_100
                                )
                            }


                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                            )


                            LinearProgressIndicator(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(16.dp)
                            )


                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                            ) {
                                FilledButtonGradient(
                                    text = stringResource(R.string.cancel_ride),
                                    isBorder = true,
                                    borderColor = MyColors.clr_00BCF1_100,
                                    buttonHeight = 40.dp,
                                    textColor = MyColors.clr_00BCF1_100,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(16.dp)
                                )
                                FilledButtonGradient(
                                    text = stringResource(R.string.accept_ride),
                                    buttonHeight = 40.dp,
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            }


                            Spacer(
                                modifier = Modifier
                                    .height(16.dp)
                            )

                        }
                    }

                }
            }
        }




    }

    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {

        },
    )




}

