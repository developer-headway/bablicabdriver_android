package com.headway.bablicabdriver.screen.dashboard.settings.refreshment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.RoundedCorner
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts

data class RefreshmentItem(
    val id: Int,
    val name: String,
    val unit: String,
    val qty: String,
    val price: String   // selling price per unit
)

@Composable
fun RefreshmentScreen(navHostController: NavHostController) {

    val dummyItems = listOf(
        RefreshmentItem(1, "Water Bottle",   "Bottle", "10", "10"),
        RefreshmentItem(2, "Juice Pack",     "Pack",   "5",  "25"),
        RefreshmentItem(3, "Biscuit Pack",   "Pack",   "8",  "15"),
        RefreshmentItem(4, "Tissue Box",     "Box",    "2",  "30"),
        RefreshmentItem(5, "Hand Sanitizer", "Bottle", "3",  "50"),
        RefreshmentItem(6, "Mint Candy",     "Piece",  "20", "2"),
    )

    var qtys   by rememberSaveable { mutableStateOf(dummyItems.map { it.qty }) }
    var errors by rememberSaveable { mutableStateOf(List(dummyItems.size) { "" }) }

    // Original qtys to detect changes
    val originalQtys = dummyItems.map { it.qty }
    val hasChanges = qtys != originalQtys

    fun validate(): Boolean {
        val newErrors = qtys.mapIndexed { i, q ->
            when {
                q.isBlank()             -> "Qty cannot be empty"
                q.toIntOrNull() == null -> "Enter a valid number"
                q.toInt() < 0           -> "Cannot be negative"
                q.toInt() > 9999        -> "Max 9999 allowed"
                else                    -> ""
            }
        }
        errors = newErrors
        return newErrors.all { it.isEmpty() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MyColors.clr_F7F7F7_100,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = "Refreshment",
                onBackPress = { navHostController.popBackStack() }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MyColors.clr_white_100)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                FilledButtonGradient(
                    text = "Save Changes",
                    alfa = if (hasChanges) 1f else 0.4f,
                    onClick = {
                        if (hasChanges && validate()) {
                            // TODO: API call
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(dummyItems) { index, item ->
                RefreshmentCard(
                    item = item,
                    qty = qtys[index],
                    error = errors[index],
                    onQtyChange = { newQty ->
                        qtys   = qtys.toMutableList().also   { it[index] = newQty }
                        errors = errors.toMutableList().also { it[index] = "" }
                    }
                )
            }
        }
    }
}

@Composable
private fun RefreshmentCard(
    item: RefreshmentItem,
    qty: String,
    error: String,
    onQtyChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neu(
                shape = Flat(RoundedCorner(14.dp)),
                lightShadowColor = MyColors.clr_7E7E7E_13,
                darkShadowColor  = MyColors.clr_7E7E7E_13,
                shadowElevation  = 2.dp
            )
            .clip(RoundedCornerShape(14.dp))
            .background(MyColors.clr_white_100)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: colored dot + name + price
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MyColors.clr_00BCF1_100, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextView(
                        text = item.name,
                        textColor = MyColors.clr_132234_100,
                        fontFamily = MyFonts.fontSemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                TextView(
                    text = "₹${item.price} / ${item.unit}",
                    textColor = MyColors.clr_08875D_100,
                    fontFamily = MyFonts.fontMedium,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Right: always stepper
            QtyStepper(
                qty = qty,
                onQtyChange = onQtyChange,
                hasError = error.isNotEmpty()
            )
        }

        // Error strip at bottom of card
        AnimatedVisibility(
            visible = error.isNotEmpty(),
            enter = slideInVertically { -it } + fadeIn(),
            exit  = slideOutVertically { -it } + fadeOut()
        ) {
            Column {
                HorizontalDivider(color = MyColors.clr_FA4949_100.copy(alpha = 0.3f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MyColors.clr_FA4949_100.copy(alpha = 0.06f))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextView(
                        text = "⚠ $error",
                        textColor = MyColors.clr_FA4949_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 11.sp,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun QtyStepper(
    qty: String,
    onQtyChange: (String) -> Unit,
    hasError: Boolean
) {
    val current = qty.toIntOrNull() ?: 0

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Minus button
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = if (current > 0) MyColors.clr_00BCF1_100 else MyColors.clr_D3DDE7_100,
                    shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                )
                .clickable {
                    if (current > 0) onQtyChange("${current - 1}")
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease",
                tint = MyColors.clr_white_100,
                modifier = Modifier.size(16.dp)
            )
        }

        // Qty input field
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(34.dp)
                .background(MyColors.clr_white_100)
                .border(
                    width = 1.dp,
                    color = if (hasError) MyColors.clr_FA4949_100 else MyColors.clr_D3DDE7_100
                ),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = qty,
                onValueChange = { value ->
                    if (value.length <= 4 && value.all { it.isDigit() }) {
                        onQtyChange(value)
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontFamily = MyFonts.fontSemiBold,
                    fontSize = 14.sp,
                    color = if (hasError) MyColors.clr_FA4949_100 else MyColors.clr_132234_100,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Plus button
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = if (current < 9999) MyColors.clr_00BCF1_100 else MyColors.clr_D3DDE7_100,
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                )
                .clickable {
                    if (current < 9999) onQtyChange("${current + 1}")
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase",
                tint = MyColors.clr_white_100,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
