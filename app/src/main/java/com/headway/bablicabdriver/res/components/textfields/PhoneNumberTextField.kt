package com.headway.bablicabdriver.res.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun PhoneNumberTextField(
    state : TextFieldState,
    maxTextCount: Int = Int.MAX_VALUE,
    textFiledHeight : Dp = 44.dp,
    borderColor : Color = MyColors.clr_E8E8E8_100,
    backgroundColor : Color = MyColors.clr_FAFAFA_100,
    modifier: Modifier = Modifier,
    placeHolder : String = "",
    placeHolderColor: Color = MyColors.clr_5A5A5A_100,
    placeHolderFontSize : TextUnit = 14.sp,
    placeHolderFontFamily: FontFamily = MyFonts.fontRegular,
    textColor: Color = MyColors.clr_5A5A5A_100,
    textFontSize : TextUnit = 14.sp,
    textFontFamily: FontFamily = MyFonts.fontRegular,
    isTyping: () -> Unit = {},
    onClick : () -> Unit = {},
    isTypeNumeric: Boolean = true,
    isLast: Boolean = false,
    enabled: Boolean = true,
    showHeading: Boolean = true,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val keyboardOptions = KeyboardOptions(
        keyboardType = if (isTypeNumeric) KeyboardType.Number else KeyboardType.Text,
        imeAction = if (isLast) ImeAction.Done else ImeAction.Next
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(textFiledHeight)
            .then(
                if (enabled) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier.background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            )
            .padding(horizontal = 12.dp)
    ) {


        BasicTextField(
            state = state,
            enabled = enabled,
            onTextLayout = {
                // Clean: remove all non-digits (including spaces)
                val digitsOnly = state.text.filter { it.isDigit() }.take(maxTextCount)
                if (state.text != digitsOnly) {
                    state.edit {
                        replace(0, length, digitsOnly)
                    }
                }

                isTyping()
            },
            modifier = Modifier,
            textStyle = TextStyle(
                fontSize = textFontSize,
                fontFamily = textFontFamily,
                color = textColor,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = { imeAction->
                onClick()
                if (isLast) {
                    focusManager.clearFocus(true)
                } else {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            },
            interactionSource = interactionSource,
            decorator = { innerTextField ->
               /* Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        TextView(
                            text = placeHolder,
                            fontSize = placeHolderFontSize,
                            textColor = placeHolderColor,
                            fontFamily = placeHolderFontFamily,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }*/




                Row (
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if ((isFocused || state.text.isNotEmpty()) && showHeading) {
                        TextView(
                            text = "91 ",
                            fontSize = textFontSize,
                            fontFamily = textFontFamily,
                            textColor = textColor,
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.text.isEmpty()) {
                            TextView(
                                text = placeHolder,
                                fontSize = placeHolderFontSize,
                                textColor = placeHolderColor,
                                fontFamily = placeHolderFontFamily,
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }

                }


            }
        )

    }

}