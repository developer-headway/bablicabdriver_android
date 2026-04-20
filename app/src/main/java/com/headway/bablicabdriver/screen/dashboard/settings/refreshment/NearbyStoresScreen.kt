package com.headway.bablicabdriver.screen.dashboard.settings.refreshment

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun NearbyStoresScreen(navHostController: NavHostController) {

    val context = LocalContext.current

    // Dummy stores - replace with API
    val stores = listOf(
        NearbyStore(1, "Quick Mart",        "Shop 12, MG Road, Bhopal",     "9876543210", 23.2599, 77.4126),
        NearbyStore(2, "Daily Needs Store", "Near Bus Stand, Arera Colony", "9812345678", 23.2450, 77.4200),
        NearbyStore(3, "Fresh Supplies",    "Plot 5, Hoshangabad Road",     "9898989898", 23.2700, 77.3900),
        NearbyStore(4, "City Mart",         "Sector 7, Bhopal",             "9811223344", 23.2550, 77.4050),
        NearbyStore(5, "Super Store",       "MP Nagar Zone 1, Bhopal",      "9833445566", 23.2480, 77.4300),
    )

    fun openGoogleMaps(lat: Double, lng: Double, name: String) {
        val uri    = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(name)})")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply { setPackage("com.google.android.apps.maps") }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=$lat,$lng")))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_F7F7F7_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = "Nearby Stores",
                onBackPress = { navHostController.popBackStack() }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(stores) { _, store ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neu(
                            shape = Flat(RoundedCorner(14.dp)),
                            lightShadowColor = MyColors.clr_7E7E7E_13,
                            darkShadowColor  = MyColors.clr_7E7E7E_13,
                            shadowElevation  = 2.dp
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(MyColors.clr_white_100)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Store icon
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(MyColors.clr_00BCF1_20, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = null,
                                tint = MyColors.clr_00BCF1_100,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Name + address + phone
                        Column(modifier = Modifier.weight(1f)) {
                            TextView(
                                text = store.name,
                                textColor = MyColors.clr_132234_100,
                                fontFamily = MyFonts.fontSemiBold,
                                fontSize = 14.sp,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            TextView(
                                text = store.address,
                                textColor = MyColors.clr_607080_100,
                                fontFamily = MyFonts.fontRegular,
                                fontSize = 12.sp,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Call row
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        context.startActivity(
                                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:${store.phone}"))
                                        )
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_call),
                                    contentDescription = "Call",
                                    modifier = Modifier.size(14.dp),
                                    colorFilter = ColorFilter.tint(MyColors.clr_00BCF1_100)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                TextView(
                                    text = store.phone,
                                    textColor = MyColors.clr_00BCF1_100,
                                    fontFamily = MyFonts.fontMedium,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Direction button
                        Column(
                            modifier = Modifier
                                .size(44.dp)
                                .background(MyColors.clr_36C10C_10, RoundedCornerShape(12.dp))
                                .clickable { openGoogleMaps(store.lat, store.lng, store.name) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_gps),
                                contentDescription = "Direction",
                                modifier = Modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MyColors.clr_36C10C_100)
                            )
                        }
                    }
                }
            }
        }
    }
}
