package com.headway.bablicabdriver.screen.dashboard.settings.myvehicle

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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import dev.materii.pullrefresh.PullRefreshIndicator
import dev.materii.pullrefresh.pullRefresh
import dev.materii.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsScreen(
    navHostController: NavHostController,
    vehicleId: String? = null
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Sample data - replace with actual API data based on vehicleId
    val vehicleDetails = remember {
        VehicleDetails(
            vehicleNumber = "GJ 01 XYZ 01234",
            babliVehicleCode = "BABALI - 123",
            rcBookFrontImage = R.drawable.ic_placeholder, // Replace with actual image
            rcBookBackImage = R.drawable.ic_placeholder,  // Replace with actual image
            assignedDrivers = listOf(
                AssignedDriver(
                    id = "1",
                    name = "Juanita Valencia",
                    shift = "10:00 AM - 02:00 PM",
                    profileImage = R.drawable.ic_placeholder
                ),
                AssignedDriver(
                    id = "2",
                    name = "Juanita Valencia",
                    shift = "10:00 AM - 02:00 PM",
                    profileImage = R.drawable.ic_placeholder
                )
            )
        )
    }

    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(1500)
                // Call API to refresh vehicle details here
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
                    text = stringResource(R.string.vehicle_details),
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
                    .pullRefresh(refreshState)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Vehicle Number Section
                item {
                    VehicleInfoSection(
                        title = stringResource(R.string.vehicle_no),
                        value = vehicleDetails.vehicleNumber
                    )
                }

                // Babli Vehicle Code Section
                item {
                    VehicleInfoSection(
                        title = stringResource(R.string.babli_vehicle_code),
                        value = vehicleDetails.babliVehicleCode
                    )
                }

                // RC Book Image Section
                item {
                    RCBookImageSection(
                        frontImage = vehicleDetails.rcBookFrontImage,
                        backImage = vehicleDetails.rcBookBackImage
                    )
                }

                // Assigned Driver Section
                item {
                    AssignedDriverSection(
                        drivers = vehicleDetails.assignedDrivers
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState
            )
        }
    }
}

@Composable
fun VehicleInfoSection(
    title: String,
    value: String
) {
    Column {
        TextView(
            text = title,
            textColor = MyColors.clr_132234_100.copy(alpha = 0.6f),
            fontFamily = MyFonts.fontRegular,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MyColors.clr_F5F5F5_100,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            TextView(
                text = value,
                textColor = MyColors.clr_132234_100,
                fontFamily = MyFonts.fontRegular,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun RCBookImageSection(
    frontImage: Int,
    backImage: Int
) {
    Column {
        TextView(
            text = stringResource(R.string.rc_book_image),
            textColor = MyColors.clr_132234_100,
            fontFamily = MyFonts.fontSemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Front Side
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TextView(
                    text = stringResource(R.string.front_side),
                    textColor = MyColors.clr_132234_100.copy(alpha = 0.6f),
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(frontImage),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MyColors.clr_F5F5F5_100),
                    contentScale = ContentScale.Crop
                )
            }

            // Back Side
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TextView(
                    text = stringResource(R.string.back_side),
                    textColor = MyColors.clr_132234_100.copy(alpha = 0.6f),
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(backImage),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MyColors.clr_F5F5F5_100),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun AssignedDriverSection(
    drivers: List<AssignedDriver>
) {
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MyColors.clr_DCDCDC_100
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextView(
            text = stringResource(R.string.assigned_driver),
            textColor = MyColors.clr_132234_100,
            fontFamily = MyFonts.fontSemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        drivers.forEach { driver ->
            DriverItem(
                driver = driver,
                onClick = {
                    // Handle driver click
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DriverItem(
    driver: AssignedDriver,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(driver.profileImage),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MyColors.clr_F5F5F5_100),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                TextView(
                    text = driver.name,
                    textColor = MyColors.clr_132234_100,
                    fontFamily = MyFonts.fontSemiBold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                TextView(
                    text = "Shift: ${driver.shift}",
                    textColor = MyColors.clr_132234_100.copy(alpha = 0.6f),
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 14.sp
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = stringResource(R.string.img_des),
            modifier = Modifier.size(20.dp),
            tint = MyColors.clr_132234_100
        )
    }
}

data class VehicleDetails(
    val vehicleNumber: String,
    val babliVehicleCode: String,
    val rcBookFrontImage: Int,
    val rcBookBackImage: Int,
    val assignedDrivers: List<AssignedDriver>
)

data class AssignedDriver(
    val id: String,
    val name: String,
    val shift: String,
    val profileImage: Int
)