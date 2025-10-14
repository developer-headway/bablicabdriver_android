package com.headway.bablicabdriver.res.components.buttons

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.headway.bablicabdriver.ui.theme.MyColors

@Composable
fun CustomSwitchButton1(
    switchPadding: Dp = 2.dp,
    buttonWidth: Dp = 46.dp,
    buttonHeight: Dp = 24.dp,
    value: Boolean,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val switchSize = buttonHeight - switchPadding * 2
    val interactionSource = remember { MutableInteractionSource() }

    val padding = if (value) buttonWidth - switchSize - switchPadding * 2 else 0.dp

    val animateSize by animateDpAsState(
        targetValue = padding,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = "switchAnim"
    )

    Box(
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .clip(CircleShape)
            .background(if (value) MyColors.clr_00BCF1_100 else MyColors.clr_E8E8E8_100)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onCheckedChange(!value)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(switchPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animateSize)
                    .background(Color.Transparent)
            )

            Box(
                modifier = Modifier
                    .size(switchSize)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}
