package com.headway.bablicabdriver.screen.dashboard

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.screen.dashboard.home.HomeScreen
import com.headway.bablicabdriver.screen.dashboard.myride.MyRideScreen
import com.headway.bablicabdriver.screen.dashboard.settings.SettingsScreen
import com.headway.bablicabdriver.screen.dashboard.wallet.WalletScreen
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.screen.dashboard.ownerDashboardScreen.OwnerDashboardScreen
import kotlinx.coroutines.launch
import kotlin.collections.forEachIndexed


@Composable
fun DashboardScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    var selBottomTab by rememberSaveable  {
        mutableStateOf(R.string.home)
    }
    val bottomTabModel = remember {
        mutableStateListOf(
            BottomTab.obj1,
            BottomTab.obj2,
            BottomTab.obj3,
            BottomTab.obj4,
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        4
    }

    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }

    BackHandler {
        if (currentPage!=0) {
            scope.launch {
                pagerState.scrollToPage(0)
                selBottomTab = R.string.home
            }
        } else {
            activity?.finishAffinity()
        }
    }

    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (sharedPreferenceManager.getUserType().lowercase()=="owner" && !sharedPreferenceManager.getIsOwnerDriver()) {
                OwnerDashboardScreen(
                    navHostController = navHostController
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        state = pagerState,
                        userScrollEnabled = false
                    ) { page: Int ->
                        when(page) {
                            0->  HomeScreen(navHostController, mainViewModel, errorStates)
                            1->  MyRideScreen(navHostController, mainViewModel, errorStates)
                            2->  WalletScreen(navHostController, mainViewModel, errorStates)
                            3->  SettingsScreen(navHostController, mainViewModel)
                            else -> {}
                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .neu(
                                lightShadowColor = MyColors.clr_7E7E7E_13,
                                darkShadowColor = MyColors.clr_7E7E7E_13,
                                shape = Flat(RoundedCorner(20.dp)),
                                shadowElevation = 2.dp
                            )
                            .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                            .background(
                                color = MyColors.clr_white_100,
                                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                            ),
                    ) {
                        bottomTabModel.forEachIndexed { index, item ->
                            var isScaled by remember { mutableStateOf(false) }
                            isScaled = selBottomTab==item.title
                            val scale by animateFloatAsState(targetValue = if (isScaled) 1.4f else 1f)
                            Box (
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        interactionSource = null,
                                        indication = null
                                    ) {
                                        selBottomTab = item.title
                                        scope.launch {
                                            pagerState.scrollToPage(index)
                                        }
                                    }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {




                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(if(selBottomTab==item.title) item.selIcon else item.icon),
                                        contentDescription = stringResource(item.title),
                                        modifier = Modifier
                                            .size(20.dp)
                                            .graphicsLayer {
                                                scaleX = scale
                                                scaleY = scale
                                            },
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                    )
                                    TextView(
                                        modifier = Modifier,
                                        text = stringResource(item.title),
                                        fontFamily = MyFonts.fontRegular,
                                        fontSize = 10.sp,
                                        textColor = if(selBottomTab==item.title) MyColors.clr_00BCF1_100 else MyColors.clr_black_100
                                    )
                                }

                            }
                        }

                    }

                }
            }

        }




    }

    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {},
    )

}




