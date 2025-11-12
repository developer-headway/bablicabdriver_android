package com.headway.bablicabdriver.screen.dashboard.home.completeRide

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun PaymentSuccessScreen(
    navHostController: NavHostController
) {
    Scaffold(
        modifier = Modifier,
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                FilledButtonGradient(
                    text = "OK",
                    backgroundColor = MyColors.clr_00BCF1_100,
                    textColor = Color.White,
                    radius = 12.dp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    onClick = {
                        navHostController.navigate(Routes.DashboardScreen.route){
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )
            }

        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_success),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier
                        .size(120.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextView(
                    text = stringResource(R.string.your_payment_is_successful),
                    textColor = MyColors.clr_black_100,
                    fontSize = 20.sp,
                    fontFamily = MyFonts.fontSemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextView(
                    text = stringResource(R.string.your_account_is),
                    textColor = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = MyFonts.fontRegular,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

            }
        }

    }
}
