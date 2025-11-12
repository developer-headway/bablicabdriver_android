package com.headway.bablicabdriver.res.components.dialog


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.textfields.FilledTextField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawAmountDialog(
    visible: MutableState<Boolean>,
    onSubmit: (String?) -> Unit = {}
) {
    val amount = rememberTextFieldState()
    var amountError by rememberSaveable {
        mutableStateOf(false)
    }


    if (visible.value) {
        Dialog(
            onDismissRequest = {

                visible.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = true // 👈 makes it full screen,

            )
        ) {

            Column(
                modifier =  Modifier
                    .fillMaxWidth()
                    .background(
                        color = MyColors.clr_white_100,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {

                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )

                TextView(
                    text = stringResource(R.string.enter_amount),
                    textColor = MyColors.clr_364B63_100,
                    fontSize = 16.sp,
                    fontFamily = MyFonts.fontBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )

                TextView(
                    text = stringResource(R.string.please_enter_amount),
                    textColor = MyColors.clr_364B63_100,
                    fontSize = 12.sp,
                    fontFamily = MyFonts.fontRegular,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )


                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )


                FilledTextField(
                    state = amount,
                    placeHolder = "Enter Amount",
                    isTyping = {
                        amountError = false
                    },
                    borderColor = MyColors.clr_E8E8E8_100,
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    textFontFamily = MyFonts.fontMedium,
                    textColor = MyColors.clr_5A5A5A_100,
                    textFontSize = 14.sp,
                    isLast = true,
                    isTypeNumeric = true
                )

                TextView(
                    text =  if(amountError) { stringResource(R.string.this_field_can_not_be_empty) } else "",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .padding(horizontal = 20.dp)
                        .height(18.dp),
                    fontSize = 10.sp,
                    fontFamily = MyFonts.fontRegular,
                    textColor = MyColors.clr_FA4949_100
                )

                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box (
                        modifier = Modifier
                            .clickable {
                                visible.value = false
                            }
                            .padding(5.dp),
                    ) {
                        TextView(
                            textColor = MyColors.clr_00BCF1_100,
                            text = stringResource(R.string.cancel),
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(20.dp)
                    )
                    Box (
                        modifier = Modifier
                            .clickable {
                                if (amount.text.isNotEmpty()) {
                                    visible.value = false
                                    onSubmit(amount.text.trim().toString())
                                } else {
                                    amountError = true
                                }
                            }
                            .padding(5.dp),
                    ) {
                        TextView(
                            textColor = MyColors.clr_00BCF1_100,
                            text = stringResource(R.string.submit),
                            fontFamily = MyFonts.fontSemiBold,
                            fontSize = 14.sp
                        )
                    }

                }

                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )


            }

        }
    }


}
