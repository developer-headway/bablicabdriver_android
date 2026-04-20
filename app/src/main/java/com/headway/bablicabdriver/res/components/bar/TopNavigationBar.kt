package com.headway.bablicabdriver.res.components.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun TopNavigationBar(
    title: String,
    showBackIcon: Boolean = true,
    backIcon: Int = R.drawable.ic_back,
    onBackPress: () -> Unit = {}
) {
    var clickAction by rememberSaveable {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()
    Column {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
//                .neu(
//                    lightShadowColor =  MyColors.clr_7E7E7E_13,
//                    darkShadowColor =  MyColors.clr_7E7E7E_13,
//                    shadowElevation = 2.dp
//                )
                .background(
                    color = MyColors.clr_white_100,
                )
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            if (showBackIcon) {
                IconButton(
                    onClick = {

                        if (!clickAction) return@IconButton
                        scope.launch {
                            clickAction = false
                            onBackPress()
                            delay(500)
                            clickAction = true
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(backIcon),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(22.dp)
                            .clip(shape = CircleShape),
                        colorFilter = ColorFilter.tint(color = MyColors.clr_black_100)
                    )
                }
            } else {
                Spacer(
                    modifier = Modifier
                        .width(24.dp)
                )
            }



            TextView(
                text = title,
                textColor = MyColors.clr_black_100,
                fontSize = 18.sp,
                fontFamily = MyFonts.fontMedium,
                modifier = Modifier
            )


        }
    }


}