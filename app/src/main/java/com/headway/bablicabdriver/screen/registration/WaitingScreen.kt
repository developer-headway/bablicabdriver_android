package com.headway.bablicabdriver.screen.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun WaitingScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.back),
                onBackPress = {
                    navHostController.popBackStack()
                }
            )
        },
        containerColor = MyColors.clr_white_100
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier
                    .height(86.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_waiting_image),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .height(260.dp)
                    .width(250.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(
                modifier = Modifier
                    .height(36.dp)
            )

            TextView(
                text = stringResource(R.string.your_profile_will),
                textColor = MyColors.clr_364B63_100,
                fontSize = 24.sp,
                fontFamily = MyFonts.fontSemiBold,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                lineHeight = 34.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier
                    .height(70.dp)
            )

            FilledButtonGradient(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    navHostController.popBackStack()
                },
                text = stringResource(R.string.ok)
            )

        }
    }


}