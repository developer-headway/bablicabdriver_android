package com.headway.bablicabdriver.screen.dashboard.settings.myvehicle

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVehicleScreen(
    navHostController: NavHostController,
    onVehicleSelected: (Vehicle) -> Unit = {}
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Sample data - replace with actual API data
    val vehicles = remember {
        listOf(
            Vehicle(
                id = "1",
                vehicleNumber = "GJ 01 XYZ 01234"
            ),
            Vehicle(
                id = "2",
                vehicleNumber = "GJ 01 XYZ 01234"
            )
        )
    }

    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(1500)
                // Call API to refresh vehicles here
                isRefreshing = false
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MyColors.clr_white_100)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navHostController.popBackStack()
                        },
                    tint = MyColors.clr_132234_100
                )

                Spacer(modifier = Modifier.width(16.dp))

                TextView(
                    text = stringResource(R.string.my_vehicle),
                    textColor = MyColors.clr_132234_100,
                    fontFamily = MyFonts.fontBold,
                    fontSize = 20.sp
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(refreshState),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(vehicles) { item ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onVehicleSelected(item)
                                    navHostController.navigate(
                                        Routes.VehicleDetailsScreen.createRoute(item.id)
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                                .background(
                                    color = MyColors.clr_white_100,
                                    shape = RoundedCornerShape(0.dp)
                                )
                                .padding(vertical = 12.dp)
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                            ) {
                                TextView(
                                    text = item.vehicleNumber,
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 16.sp
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(2.dp)
                                )
                                TextView(
                                    text = "Shift: 10:00 AM - 02:00 PM",
                                    textColor = MyColors.clr_132234_100,
                                    fontFamily = MyFonts.fontRegular,
                                    fontSize = 12.sp
                                )
                            }


                            Icon(
                                painter = painterResource(R.drawable.ic_next_arrow),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier.size(20.dp),
                                tint = MyColors.clr_132234_100
                            )
                        }
                        HorizontalDivider(
                            color = MyColors.clr_00BCF1_20,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }


                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState
            )
        }
    }
}



data class Vehicle(
    val id: String,
    val vehicleNumber: String
)
