package com.headway.bablicabdriver.screen.dashboard.home
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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textfields.FilledTextField
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
fun SetRouteScreen(
    navHostController: NavHostController,
    onRouteSelected: (ShuttleRoute) -> Unit = {}
) {
    val searchQuery = rememberTextFieldState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Sample data - replace with actual API data
    val routes = remember {
        listOf(
            ShuttleRoute(
                id = "1",
                fromLocation = "Bus Sta Upas, Majestic, Bengaluru, Karnataka 560009",
                toLocation = "M.G. Railway Colony, Majestic, Bengaluru, Karnataka 560023"
            ),
            ShuttleRoute(
                id = "2",
                fromLocation = "Bus Sta Upas, Majestic, Bengaluru, Karnataka 560009",
                toLocation = "M.G. Railway Colony, Majestic, Bengaluru, Karnataka 560023"
            ),
            ShuttleRoute(
                id = "3",
                fromLocation = "Bus Sta Upas, Majestic, Bengaluru, Karnataka 560009",
                toLocation = "M.G. Railway Colony, Majestic, Bengaluru, Karnataka 560023"
            )
        )
    }

    val filteredRoutes = routes.filter {
        it.fromLocation.contains(searchQuery.text.toString(), ignoreCase = true) ||
                it.toLocation.contains(searchQuery.text.toString(), ignoreCase = true)
    }

    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(1500)
                // Call API to refresh routes here
                isRefreshing = false
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_white_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = stringResource(R.string.set_route),
                onBackPress = {
                    navHostController.popBackStack()
                }
            )

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(refreshState)
            ) {


                Row (
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MyColors.clr_00BCF1_100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(6.dp)
                    )
                    FilledTextField(
                        state = searchQuery,
                        borderColor = MyColors.clr_white_100,
                        modifier = Modifier
                            .padding(start = 4.dp),
                        horizontalPadding = 2.dp,
                        placeHolder = "Search By Route Name",
                        textFontFamily = MyFonts.fontRegular,
                        textFontSize = 14.sp
                    )
                }

                // Routes List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }

                    items(filteredRoutes) { route ->
                        RouteCard(
                            route = route,
                            onSetRoute = {
                                onRouteSelected(route)
                                navHostController.popBackStack()
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
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
fun RouteCard(
    route: ShuttleRoute,
    onSetRoute: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MyColors.clr_00BCF1_100,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MyColors.clr_white_100,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // From Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = MyColors.clr_4CAF50_100,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                TextView(
                    text = route.fromLocation,
                    textColor = MyColors.clr_132234_100,
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            // Vertical Line
            Box(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .width(2.dp)
                    .height(24.dp)
                    .background(MyColors.clr_132234_100.copy(alpha = 0.3f))
            )

            // To Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = MyColors.clr_FF0000_100,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                TextView(
                    text = route.toLocation,
                    textColor = MyColors.clr_132234_100,
                    fontFamily = MyFonts.fontRegular,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Set This Root Button
            FilledButtonGradient(
                text = stringResource(R.string.set_this_root),
                onClick = onSetRoute
            )
        }
    }
}

data class ShuttleRoute(
    val id: String,
    val fromLocation: String,
    val toLocation: String
)
