package com.headway.bablicabdriver.res.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun PermissionsRequiredDialog(
    isLocationGranted: Boolean,
    onGrantPermissions: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyColors.clr_white_100, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lock icon header
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MyColors.clr_00BCF1_10),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MyColors.clr_00BCF1_100,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextView(
                text = "Permissions Required",
                fontSize = 18.sp,
                fontFamily = MyFonts.fontBold,
                textColor = MyColors.clr_313131_100,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextView(
                text = "To mark your attendance, please allow the following permissions:",
                fontSize = 13.sp,
                fontFamily = MyFonts.fontRegular,
                textColor = MyColors.clr_7E7E7E_100,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Location permission row
            PermissionRow(
                icon = Icons.Default.LocationOn,
                iconBg = if (isLocationGranted) MyColors.clr_36C10C_10 else MyColors.clr_00BCF1_10,
                iconTint = if (isLocationGranted) MyColors.clr_36C10C_100 else MyColors.clr_00BCF1_100,
                title = "Location",
                subtitle = "Required to record your punch location",
                isGranted = isLocationGranted
            )

            Spacer(modifier = Modifier.height(24.dp))

//            Spacer(
//                modifier = Modifier
//                    .height(25.dp)
//            )

            // Grant button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MyColors.clr_00BCF1_100),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = onGrantPermissions,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextView(
                        text = "Grant Permissions",
                        fontSize = 15.sp,
                        fontFamily = MyFonts.fontBold,
                        textColor = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Cancel button
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextView(
                    text = "Cancel",
                    fontSize = 14.sp,
                    fontFamily = MyFonts.fontMedium,
                    textColor = MyColors.clr_7E7E7E_100
                )
            }


        }
    }
}

@Composable
private fun PermissionRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    isGranted: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isGranted) MyColors.clr_36C10C_10 else MyColors.clr_F7F7F7_100,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            TextView(
                text = title,
                fontSize = 14.sp,
                fontFamily = MyFonts.fontBold,
                textColor = MyColors.clr_313131_100
            )
            Spacer(modifier = Modifier.height(2.dp))
            TextView(
                text = subtitle,
                fontSize = 12.sp,
                fontFamily = MyFonts.fontRegular,
                textColor = MyColors.clr_7E7E7E_100
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Status badge
        Box(
            modifier = Modifier
                .background(
                    color = if (isGranted) MyColors.clr_36C10C_100 else MyColors.clr_FA4949_100,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            TextView(
                text = if (isGranted) "Granted" else "Required",
                fontSize = 11.sp,
                fontFamily = MyFonts.fontSemiBold,
                textColor = Color.White
            )
        }
    }
}
