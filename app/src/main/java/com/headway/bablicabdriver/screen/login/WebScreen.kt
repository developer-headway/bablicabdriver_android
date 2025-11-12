package com.headway.bablicabdriver.screen.login

import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.ui.theme.MyColors

@Composable
fun WebScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val url = navHostController.previousBackStackEntry?.savedStateHandle?.get("url")?:""
    val title = navHostController.previousBackStackEntry?.savedStateHandle?.get("title")?:""

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopNavigationBar(
                title = title,
                onBackPress = {
                    navHostController.popBackStack()
                }
            )
        },
        containerColor = MyColors.clr_white_100
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {

            // Adding a WebView inside AndroidView
            // with layout as full screen
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    loadUrl(url)
                }
            }, update = {
                it.loadUrl(url)
            })
        }



    }

}
