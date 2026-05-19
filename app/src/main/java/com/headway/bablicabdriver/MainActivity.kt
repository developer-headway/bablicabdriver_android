package com.headway.bablicabdriver

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.headway.bablicabdriver.res.components.bar.ShowSnackBar
import com.headway.bablicabdriver.screen.navgraph.NavigationGraph
import com.headway.bablicabdriver.ui.theme.BabliCabDriverTheme
import com.headway.bablicabdriver.utils.KeyBoardManager
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.razorpay.PaymentResultListener


class MainActivity : ComponentActivity(), PaymentResultListener {
    val mainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BabliCabDriverTheme {
                val context = LocalContext.current
                val focusManager = LocalFocusManager.current
                val removePadding by mainViewModel.removePadding.collectAsState()
                val showSnackBar by mainViewModel.showSnackBar.collectAsState()
                val snackBarText by mainViewModel.snackBarText.collectAsState()

                //when keyboard is closed, focus will be cleared.
                DisposableEffect(key1 = context) {
                    val keyboardManager = KeyBoardManager(context)
                    keyboardManager.attachKeyboardDismissListener {
                        focusManager.clearFocus()
                    }
                    onDispose {
                        keyboardManager.release()
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures {
                                // when user clicks whenever rather than textfield
                                //than focus will be cleared from all textfields on the screen.
                                focusManager.clearFocus(true)
                            }
                        }
                        .navigationBarsPadding(),
                    snackbarHost = {
                        ShowSnackBar(
                            message = snackBarText,
                            showSb = showSnackBar,
                            openSnackBar = {
                                mainViewModel.updateSnackBar(false)
                            }
                        )
                    },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (removePadding) {
                                    Modifier
                                        .navigationBarsPadding()
                                } else {
                                    Modifier
                                        .padding(innerPadding)
                                }
                            )
                    ) {
                        NavigationGraph(mainViewModel,)
                    }
                }
            }
        }
    }



    override fun onPaymentSuccess(p0: String?) {
        Log.d("msg", "Payment success: $p0")
        mainViewModel.onPaymentSuccess(p0)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.d("msg", "Payment Error: $p1")
        mainViewModel.onPaymentError(p1)
    }
}

