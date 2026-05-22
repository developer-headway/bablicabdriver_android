package com.headway.bablicabdriver.res.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternalServerErrorDialog(
    visible : MutableState<Boolean>,
    onCreateTicket : () -> Unit,
    onUnderstood : () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()
    val skipPartiallyExpanded by remember { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
    )
    LaunchedEffect(key1 = visible.value, block = {
        focusManager.clearFocus(true)
        if (visible.value){
            bottomSheetState.expand()
            bottomSheetState.show()
        }
    })

    if (visible.value) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),
            onDismissRequest = {
                scope.launch {
                    visible.value = true
                    bottomSheetState.expand()
                    bottomSheetState.show()
                }
            },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(24.dp),
            dragHandle = {},
            containerColor = Color.Transparent,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false,
                shouldDismissOnClickOutside = false,
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .padding(10.dp, 10.dp)
                        .widthIn(max = 500.dp)
                        .fillMaxWidth()
                        .background(
                            color = MyColors.clr_white_100,
                            shape = RoundedCornerShape(20.dp)
                        )
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
                            text = stringResource(R.string.something_went_wrong),
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 16.sp,
                            color = MyColors.clr_00BCF1_100,
                            letterSpacing = 0.sp,
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_login_again),
                            contentDescription = "",
                            Modifier.size(20.dp),
                            tint = MyColors.clr_00BCF1_100
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    val text = buildAnnotatedString {
                        append(stringResource(R.string.please_try_again_we_are_committed_to_providing_you_with_the_best_possible_service_if_the_issue_continues_to_accrue))
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append(stringResource(R.string.please_contact_us_by_creating_a_ticket))
                        }
                    }
                    Text(
                        text = stringResource(R.string.please_try_again_we_are_committed_to_providing_you_with_the_best_possible_service_if_the_issue_continues_to_accrue),
                        fontFamily = MyFonts.fontMedium,
                        fontSize = 12.sp,
                        letterSpacing = 0.sp,
                        lineHeight = 18.sp,
                        color = MyColors.clr_7E7E7E_100,
                    )
                    Spacer(modifier = Modifier.height(40.dp))


                    FilledButton(
                        onClick = {
                            visible.value = false
                            onUnderstood()
                        },
                        text = stringResource(R.string.ok_understood)
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}