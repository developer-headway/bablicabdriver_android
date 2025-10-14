package com.headway.bablicabdriver.model.dashboard

import com.headway.bablicabdriver.R


data class BottomTabModel(
    val title : Int,
    val icon : Int,
    val selIcon : Int
)

object BottomTab {
    val obj1 = BottomTabModel(
        title = R.string.home,
        icon = R.drawable.ic_home,
        selIcon = R.drawable.ic_home_sel
    )
    val obj2 = BottomTabModel(
        title = R.string.wallet,
        icon = R.drawable.ic_wallet,
        selIcon = R.drawable.ic_wallet_sel
    )
    val obj3 = BottomTabModel(
        title = R.string.rewards,
        icon = R.drawable.ic_reward,
        selIcon = R.drawable.ic_reward_sel
    )
    val obj4 = BottomTabModel(
        title = R.string.setting,
        icon = R.drawable.ic_setting,
        selIcon = R.drawable.ic_setting_sel
    )
}