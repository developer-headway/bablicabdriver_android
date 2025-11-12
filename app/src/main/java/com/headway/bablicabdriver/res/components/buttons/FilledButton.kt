package com.headway.bablicabdriver.res.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun FilledButton(
    text: String = "",
    buttonHeight: Dp = 44.dp,
    backgroundColor: Color = MyColors.clr_F6821F_100,
    isBorder: Boolean = false,
    borderColor: Color = MyColors.clr_F6821F_100,
    modifier: Modifier = Modifier,
    textColor: Color = MyColors.clr_white_100,
    textFontSize: TextUnit = 14.sp,
    textFontFamily: FontFamily = MyFonts.fontBold,
    radius: Dp = 8.dp,
    showHeadingIcon: Boolean = false,
    headingIcon: Int = R.drawable.ic_back,
    headingIconSize: Dp = 12.dp,
    headingIconSpace: Dp = 10.dp,
    showTrailingIcon: Boolean = false,
    trailingIcon: Int = R.drawable.ic_back,
    trailingIconSize: Dp = 12.dp,
    trailingIconSpace: Dp = 10.dp,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .then(
                if (!isBorder) {
                    Modifier
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(radius)
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

            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (showHeadingIcon) {

            Image(
                painter = painterResource(headingIcon),
                contentDescription = stringResource(R.string.img_des),
                modifier = Modifier
                    .size(headingIconSize)
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