package com.headway.bablicabdriver.screen.alarm

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.MainActivity
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

class AlarmActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock screen ke upar bhi dikhao
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)

        val title = intent.getStringExtra("title") ?: "New Notification"
        val body = intent.getStringExtra("body") ?: ""

        setContent {
            AlarmScreen(
                title = title,
                body = body,
                onOpenApp = {
                    // MainActivity open karo
                    val mainIntent = Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    }
                    startActivity(mainIntent)
                    finish()
                },
                onDismiss = {
                    // Screen aur app dono band karo
                    finish()
                }
            )
        }
    }
}

@Composable
fun AlarmScreen(
    title: String,
    body: String,
    onOpenApp: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MyColors.clr_243369_100,
                        MyColors.clr_000B58_100
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Bell icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = MyColors.clr_00BCF1_100.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MyColors.clr_00BCF1_100,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = title,
                color = MyColors.clr_white_100,
                fontSize = 22.sp,
                fontFamily = MyFonts.fontBold,
                textAlign = TextAlign.Center
            )

            if (body.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = body,
                    color = MyColors.clr_white_100.copy(alpha = 0.75f),
                    fontSize = 15.sp,
                    fontFamily = MyFonts.fontRegular,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dismiss button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MyColors.clr_white_100
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, MyColors.clr_white_100.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Dismiss",
                        fontFamily = MyFonts.fontMedium,
                        fontSize = 14.sp
                    )
                }

                // Open App button
                Button(
                    onClick = onOpenApp,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyColors.clr_00BCF1_100
                    )
                ) {
                    Text(
                        text = "Open App",
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 14.sp,
                        color = MyColors.clr_white_100
                    )
                }
            }
        }
    }
}
