package com.headway.bablicabdriver.res.components.dialog


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDeleteAccountDialog(
    visible : MutableState<Boolean>,
    onLogout : () -> Unit
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )

    if (visible.value) {
        ModalBottomSheet(
            modifier = Modifier
                .padding(10.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                visible.value = false
            },
            containerColor = MyColors.clr_white_100,
            shape = RoundedCornerShape(20.dp),
            dragHandle = {}
        ) {
            Column(
                modifier =  Modifier
                    .fillMaxWidth()
            ) {

                IconButton(
                    modifier = Modifier
                        .offset(x = (-5).dp)
                        .align(Alignment.End),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            visible.value = false
                        }
                    }
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_cross),
                        contentDescription = "",
                        Modifier
                            .size(13.dp),
                        colorFilter = ColorFilter.tint(MyColors.clr_7E7E7E_100)
                    )
                }



                TextView(
                    text = stringResource(R.string.delete_account_alert),
                    fontFamily = MyFonts.fontSemiBold,
                    fontSize = 16.sp,
                    textColor = MyColors.clr_7E7E7E_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                )
                TextView(
                    text = stringResource(R.string.delete_account_alert_des),
                    fontFamily = MyFonts.fontMedium,
                    fontSize = 12.sp,
                    textColor = MyColors.clr_7E7E7E_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )
                FilledButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            visible.value = false
                        }
                    },
                    backgroundColor = MyColors.clr_E7F4FE_50,
                    text = stringResource(R.string.no_stay_here),
                    textColor =  MyColors.clr_00BCF1_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)

                )
                Spacer(modifier = Modifier.height(10.dp))

                FilledButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onLogout()
                            visible.value = false
                        }
                    },
                    backgroundColor = MyColors.clr_00BCF1_100,
                    text = stringResource(R.string.yes_delete_account),
                    textColor = MyColors.clr_white_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }


}
