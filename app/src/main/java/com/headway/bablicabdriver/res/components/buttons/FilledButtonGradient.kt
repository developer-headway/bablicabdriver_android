package com.headway.bablicabdriver.res.components.buttons

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import kotlinx.coroutines.flow.collectLatest


@Composable
fun FilledButtonGradient(
    text: String = "",
    buttonHeight: Dp = 48.dp,
    isBorder: Boolean = false,
    borderColor: Color = MyColors.clr_243369_100,
    backgroundColor: Color = MyColors.clr_00BCF1_100,
    modifier: Modifier = Modifier,
    textColor: Color = MyColors.clr_white_100,
    textFontSize: TextUnit = 14.sp,
    textFontFamily: FontFamily = MyFonts.fontSemiBold,
    radius: Dp = 12.dp,
    showHeadingIcon: Boolean = false,
    headingIcon: Int = R.drawable.ic_back,
    headingIconSize: Dp = 12.dp,
    headingIconSpace: Dp = 10.dp,
    headingIconColor: Color = MyColors.clr_white_100,
    showTrailingIcon: Boolean = false,
    innerShadow: Boolean = true,
    maxWidth: Boolean = true,
    trailingIcon: Int = R.drawable.ic_back,
    trailingIconSize: Dp = 12.dp,
    trailingIconSpace: Dp = 10.dp,
    alfa:Float= 1f,
    onClick: () -> Unit = {}
) {
    var isPressed by rememberSaveable {
        mutableStateOf(false)
    }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f, animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = ""
    )
    val interaction = remember {
        MutableInteractionSource()
    }


    LaunchedEffect(key1 = interaction) {
        interaction.interactions.collectLatest {
            when (it) {
                is PressInteraction.Press -> {
                    isPressed = true
                }

                is PressInteraction.Cancel -> {
                    isPressed = false
                }

                is PressInteraction.Release -> {
                    isPressed = false
                    onClick()
                }
            }
        }
    }


    Row(
        modifier = modifier
            .scale(scale)
            .height(buttonHeight)
            .then(
                if (maxWidth)
                    Modifier
                        .fillMaxWidth()
                else Modifier
            )
            .then(
                if (!isBorder) {
                    Modifier
                        .clip(shape = RoundedCornerShape(radius))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    backgroundColor,
                                    backgroundColor,
                                )
                            ),
                            shape = RoundedCornerShape(radius),
                            alpha = alfa
                        )
                        .then(
                            if (innerShadow){
                                Modifier
                                    .innerShadow(
                                        shape =  RoundedCornerShape(radius),
                                        shadow = Shadow(
                                            radius = 12.dp,
                                            offset =  DpOffset(0.dp, (-3).dp),
                                            alpha = 0.3f,

                                            )
                                    )
                            } else {
                                Modifier
                            }
                        )

                } else {
                    Modifier
                        .border(
                            color = borderColor,
                            shape = RoundedCornerShape(radius),
                            width = 1.dp
                        )
                }
            )
            .clickable(
                interactionSource = interaction,
                indication = null
            ) {
//                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (showHeadingIcon) {

            Image(
                painter = painterResource(headingIcon),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .size(headingIconSize),
                colorFilter = ColorFilter.tint(
                    color = headingIconColor
                )
            )
            Spacer(
                modifier = Modifier
                    .width(headingIconSpace)
            )
        }

        TextView(
            text = text,
            textColor = textColor,
            fontSize = textFontSize,
            fontFamily = textFontFamily,
            modifier = Modifier
        )

        if (showTrailingIcon) {
            Spacer(
                modifier = Modifier
                    .width(trailingIconSpace)
            )
            Image(
                painter = painterResource(trailingIcon),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .size(trailingIconSize)
            )
        }


    }

}