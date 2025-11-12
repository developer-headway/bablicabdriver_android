package com.headway.bablicabdriver.res.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@Composable
fun FilledTextField(
    state: TextFieldState,
    textFiledHeight: Dp = 44.dp,
    borderColor: Color = MyColors.clr_E8E8E8_100,
    backgroundColor: Color = MyColors.clr_FAFAFA_100,
    modifier: Modifier = Modifier,
    placeHolder: String = "",
    placeHolderColor: Color = MyColors.clr_C4C6CA_100,
    placeHolderFontSize: TextUnit = 14.sp,
    placeHolderFontFamily: FontFamily = MyFonts.fontRegular,
    textColor: Color = MyColors.clr_5A5A5A_100,
    textFontSize: TextUnit = 14.sp,
    textFontFamily: FontFamily = MyFonts.fontRegular,
    isTyping: () -> Unit = {},
    onClick: () -> Unit = {},
    isTypeNumeric: Boolean = false,
    isLast: Boolean = false,
    enabled: Boolean = true,
    maxTextCount: Int = Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Text,
    radius: Dp = 8.dp
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val keyboardOptions = KeyboardOptions(
        capitalization = if (keyboardType == KeyboardType.Text) KeyboardCapitalization.Sentences else KeyboardCapitalization.None,
        keyboardType = if (isTypeNumeric) KeyboardType.Phone else keyboardType,
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
                        shape = RoundedCornerShape(radius)
                    )
                } else {
                    Modifier.background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(radius)
                    )
                }
            )
            .padding(horizontal = 12.dp)
    ) {


        BasicTextField(
            state = state,
            enabled = enabled,
            onTextLayout = {
                val original = state.text.toString()
                var cleaned = original
                    .replace(Regex("\\s+"), " ")  // Replace multiple spaces with single space
                    .trimStart()                  // Remove leading spaces

                if (keyboardType == KeyboardType.Email) {
                    cleaned = original
                        .replace(Regex(" "), "")
                        .lowercase() // Replace multiple spaces with single space
                        .trimStart()
                }
                cleaned = cleaned.take(maxTextCount)
                if (cleaned != original) {
                    state.edit {
                        replace(0, length, cleaned)
                    }
                }

                isTyping()
            },
            modifier = Modifier
                .clickable {
                    onClick()
                },
            textStyle = TextStyle(
                fontSize = textFontSize,
                fontFamily = textFontFamily,
                color = textColor,
            ),
            onKeyboardAction = { it ->
                onClick()
                if (isLast) {
                    focusManager.clearFocus(true)
                } else {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            },
            interactionSource = interactionSource,
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = keyboardOptions,
            decorator = { innerTextField ->
                Box(
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
                }
            }
        )

    }

}

@Composable
fun FilledOutlinedTextField(
    text: MutableState<String>,
    textFiledHeight: Dp = 44.dp,
    borderColor: Color = MyColors.clr_E8E8E8_100,
    backgroundColor: Color = MyColors.clr_FAFAFA_100,
    modifier: Modifier = Modifier,
    placeHolder: String = "",
    placeHolderColor: Color = MyColors.clr_C4C6CA_100,
    placeHolderFontSize: TextUnit = 14.sp,
    placeHolderFontFamily: FontFamily = MyFonts.fontRegular,
    textColor: Color = MyColors.clr_5A5A5A_100,
    textFontSize: TextUnit = 14.sp,
    textFontFamily: FontFamily = MyFonts.fontRegular,
    isTyping: () -> Unit = {},
    onClick: () -> Unit = {},
    isTypeNumeric: Boolean = false,
    isLast: Boolean = false,
    enabled: Boolean = true,
    maxTextCount: Int = Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Text,
    radius: Dp = 12.dp
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val keyboardOptions = KeyboardOptions(
        capitalization = if (keyboardType == KeyboardType.Text) KeyboardCapitalization.Sentences else KeyboardCapitalization.None,
        keyboardType = if (isTypeNumeric) KeyboardType.Phone else keyboardType,
        imeAction = if (isLast) ImeAction.Done else ImeAction.Next
    )

//    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = text.value,
        onValueChange = {
            text.value = it
            isTyping()
        },
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(radius),
        maxLines = 1,
        label = {

            TextView(
                text = placeHolder,
                fontSize = placeHolderFontSize,
                textColor = placeHolderColor,
                fontFamily = placeHolderFontFamily,
                maxLines = 1
            )
        },
        placeholder = {
            TextView(
                text = "",
                fontSize = textFontSize,
                textColor = textColor,
                fontFamily = textFontFamily,
                maxLines = 1
            )
        },

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MyColors.clr_E9F0F7_100,
            unfocusedBorderColor = MyColors.clr_E9F0F7_100,
            disabledBorderColor = MyColors.clr_E9F0F7_100,
        )


    )

}

