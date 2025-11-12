package com.headway.bablicabdriver.res.components.textfields

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import kotlinx.coroutines.delay

@Composable
fun OtpInputField(
    otp : MutableState<String>,
    isTyping : () -> Unit = {},
) {

    var visible by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(key1 = true){
        while (true){
            visible = !visible
            delay(500)
        }
    }
    BasicTextField(
        value = otp.value, onValueChange = {
            val regex = "[0-9]*".toRegex()
            if (it.matches(regex = regex)) {
                otp.value = it.take(4)
            }
            isTyping()
        },
        enabled = true,
        textStyle = TextStyle(
            fontFamily = MyFonts.fontMedium,
            fontSize = 22.sp,
            letterSpacing = 0.sp,
            color = MyColors.clr_white_100
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        cursorBrush =  Brush.linearGradient(
            colors = listOf(
                MyColors.clr_313131_100,
                MyColors.clr_313131_100,
            )
        ),
        decorationBox = {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(4) { index ->
                    val char = when {
                        index == otp.value.length -> ""
                        index > otp.value.length -> ""
                        else -> otp.value[index].toString()
                    }
                    Box(
                        Modifier
                            .padding(end = 18.dp)
                            .size(56.dp)
                            .border(
                                width = 1.dp,
                                color = if (char.isEmpty()) MyColors.clr_E8E8E8_100 else MyColors.clr_243369_100,
                                shape = RoundedCornerShape(10.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontFamily = MyFonts.fontRegular,
                            fontSize = 18.sp,
                            letterSpacing = 0.sp,
                            color = MyColors.clr_313131_100
                        )

                        if (index == otp.value.length) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = visible && (index == otp.value.length),
                                enter = EnterTransition.None, exit = ExitTransition.None
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(2.dp)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = Modifier
            .padding(horizontal = 20.dp)
    )
}