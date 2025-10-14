package com.headway.bablicabdriver.res.components.textview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts


@Composable
fun TextView(
    text : String,
    textColor: Color = MyColors.clr_black_100,
    fontSize : TextUnit = 12.sp,
    lineHeight: TextUnit = fontSize,
    textAlign: TextAlign = TextAlign.Start,
    fontFamily: FontFamily = MyFonts.fontMedium,
    modifier : Modifier = Modifier,
    maxLines : Int = Int.MAX_VALUE,
    minLines : Int = 1,
    maxChars: Int = Int.MAX_VALUE,
    textDecoration: TextDecoration = TextDecoration.None
) {

    Text(
        modifier = modifier,
        text = text.take(maxChars),
        color = textColor,
        fontSize = fontSize,
        fontFamily = fontFamily,
        textAlign = textAlign,
        lineHeight = lineHeight,
        letterSpacing = 0.sp,
        maxLines = maxLines,
        textDecoration = textDecoration,
        overflow = TextOverflow.Ellipsis,
        minLines = minLines
    )
}

@Composable
fun TextView1(
    text : String,
    textColor: Color = MyColors.clr_black_100,
    fontSize : TextUnit = 12.sp,
    lineHeight: TextUnit = fontSize,
    textAlign: TextAlign = TextAlign.Start,
    fontFamily: FontFamily = MyFonts.fontMedium,
    modifier : Modifier = Modifier,
    maxLines : Int = Int.MAX_VALUE,
    textDecoration: TextDecoration = TextDecoration.None,
    maxChars: Int = Int.MAX_VALUE // 👈 Add this parameter
) {

    val finalText = remember(text, maxChars) {
        if (text.length > maxChars) {
            text.take(maxChars).dropLast(3).plus("...")
        } else {
            text
        }
    }

    Text(
        modifier = modifier,
        text = finalText,
        color = textColor,
        fontSize = fontSize,
        fontFamily = fontFamily,
        textAlign = textAlign,
        lineHeight = lineHeight,
        letterSpacing = 0.sp,
        maxLines = maxLines,
        textDecoration = textDecoration,
        overflow = TextOverflow.Ellipsis
    )
}