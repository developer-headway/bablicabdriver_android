package com.headway.bablicabdriver.model.intro

import com.headway.bablicabdriver.R


data class IntroModel(
    val title: Int,
    val des : Int,
    val image : Int,
)

object IntroUtils {
    val obj1 = IntroModel(
        title = R.string.intro_title1,
        des = R.string.intro_des1,
        image = R.drawable.ic_intro1
    )
    val obj2 = IntroModel(
        title = R.string.intro_title1,
        des = R.string.intro_des1,
        image = R.drawable.ic_intro1
    )
    val obj3 = IntroModel(
        title = R.string.intro_title1,
        des = R.string.intro_des1,
        image = R.drawable.ic_intro1
    )
}


