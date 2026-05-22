package com.headway.bablicabdriver.res.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.buttons.FilledButton
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun SessionExpiredDialog(
    visible: MutableState<Boolean>,
    onLoginAgain: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
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
                shouldDismissOnBackPress = false,
                shouldDismissOnClickOutside = false,
            )

        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp)
            ) {
                Spacer(modifier = Modifier.height(25.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.login_again),
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 16.sp,
                        color = MyColors.clr_7E7E7E_100,
                        letterSpacing = 0.sp,
                        lineHeight = 19.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_login_again),
                        contentDescription = "",
                        Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MyColors.clr_7E7E7E_100)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                val text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        append(stringResource(R.string.we_have_noticed_that_you_logged_in_earlier_with_a_different_device))
                    }
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(stringResource(R.string.please_login_again_to_continue))
                    }
                }
                Text(
                    text = text,
                    fontFamily = MyFonts.fontMedium,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.sp,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(40.dp))

                FilledButton(
                    onClick = {
                        scope.launch {
                            visible.value = false
                        }.invokeOnCompletion {
                            onLoginAgain()
                            AppUtils.logoutAndClearData(context = context)
                        }
                    },
                    text = stringResource(R.string.login_again),
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}