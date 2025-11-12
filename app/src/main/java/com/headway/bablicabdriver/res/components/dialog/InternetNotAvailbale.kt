package com.headway.bablicabdriver.res.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.buttons.FilledButton
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun InternetNotAvailable(
    visible: MutableState<Boolean>,
    onRetry: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            false
        }
    )

    if (visible.value) {
        ModalBottomSheet(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxWidth()
                .navigationBarsPadding()
                .statusBarsPadding(),
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    visible.value = false
                }
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(24.dp),
            dragHandle = {},
            containerColor = MyColors.clr_white_100,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false
            )

        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp)
            ) {
                Spacer(modifier = Modifier.height(25.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.no_internet_connection),
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 16.sp,
                        color = MyColors.clr_7E7E7E_100,
                        letterSpacing = 0.sp,
                        lineHeight = 19.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_no_internet),
                        contentDescription = "",
                        Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MyColors.clr_7E7E7E_100)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow (
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)) {
                    Text(
                        text = "It looks like you're offline. Please check your Wi-Fi or mobile data connection, or turn flight mode on and off, then ",
                        fontFamily = MyFonts.fontMedium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        lineHeight = 18.sp,
                        color = MyColors.clr_7E7E7E_100
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                FilledButton(
                    onClick = {

                        scope.launch {
                            visible.value = false
                        }.invokeOnCompletion {
                            onRetry()
                        }
                    },
                    backgroundColor = MyColors.clr_243369_100,
                    text = stringResource(R.string.retry),
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}