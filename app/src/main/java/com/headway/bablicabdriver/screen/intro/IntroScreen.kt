package com.headway.bablicabdriver.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.model.intro.IntroModel
import com.headway.bablicabdriver.model.intro.IntroUtils
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import mx.platacard.pagerindicator.PagerIndicator
import mx.platacard.pagerindicator.PagerIndicatorOrientation

@Composable
fun IntroScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val introModel = remember {
        mutableStateListOf(
            IntroUtils.obj1,
            IntroUtils.obj2,
            IntroUtils.obj3,
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        introModel.size
    }

    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }

    DisposableEffect(Unit) {
        mainViewModel.removePadding.value = true
        onDispose {
            mainViewModel.removePadding.value = false
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MyColors.clr_white_100
                    )
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                FilledButtonGradient(
                    text = stringResource(if (currentPage == 2) R.string.lets_start else R.string.next),
                    textColor = MyColors.clr_white_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    onClick = {
                        scope.launch {
                            when (currentPage) {
                                2 -> {
                                    sharedPreferenceManager.storeShowIntro()
                                    navHostController.navigate(Routes.LoginScreen.route) {
                                        launchSingleTop = true
                                        popUpTo(Routes.IntroScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }

                                else -> {
                                    pagerState.animateScrollToPage(currentPage + 1)
                                }
                            }
                        }
                    }
                )

                Spacer(
                    modifier = Modifier
                        .height(36.dp)
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {

            Column {
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    beyondViewportPageCount = 4,
                    key = { it },
                    userScrollEnabled = true
                ) { pageCount ->
                    IntroView(introModel[pageCount], pageCount)
                }
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )
                PagerIndicator(
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    pagerState = pagerState,
                    activeDotColor = MyColors.clr_00BCF1_100,
                    dotColor = MyColors.clr_E8E8E8_100,
                    dotCount = 6,
                    orientation = PagerIndicatorOrientation.Horizontal,
                    space = 4.dp
                )
            }

            if (currentPage != 2) {
                TextView(
                    text = stringResource(R.string.skip),
                    textColor = MyColors.clr_00BCF1_100,
                    fontSize = 16.sp,
                    fontFamily = MyFonts.fontRegular,
                    modifier = Modifier
                        .padding(top = 56.dp, end = 20.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        }
                        .padding(5.dp)
                )
            }


        }


    }

}

@Composable
fun  IntroView(
    item: IntroModel,
    page: Int,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(item.image),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(410.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        TextView(
            text = stringResource(item.title),
            textColor = MyColors.clr_132234_100,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            fontFamily = MyFonts.fontRegular,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        )
        Spacer(
            modifier = Modifier
                .height(4.dp)
        )

        TextView(
            text = stringResource(item.des),
            textColor = MyColors.clr_364B63_100,
            textAlign = TextAlign.Start,
            fontSize = 12.sp,
            fontFamily = MyFonts.fontRegular,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        )

    }

}