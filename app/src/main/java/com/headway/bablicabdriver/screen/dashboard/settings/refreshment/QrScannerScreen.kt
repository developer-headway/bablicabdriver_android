package com.headway.bablicabdriver.screen.dashboard.settings.refreshment


import android.Manifest
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.utils.permissionhandler.rememberPermissionsState
import java.util.concurrent.Executors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanQrCodeScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val lifecycleOwner = LocalLifecycleOwner.current

    var scannedCode by remember { mutableStateOf<String?>(null) }
    var isScanning by remember { mutableStateOf(true) }
    var hasPermission by remember { mutableStateOf(false) }
    var flashEnabled by remember { mutableStateOf(false) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }


    val rideId = navHostController.previousBackStackEntry?.savedStateHandle?.get<String?>("ride_id")
    val rideOtp = navHostController.previousBackStackEntry?.savedStateHandle?.get<String?>("otp")
    val vehicleIdealNumber =
        navHostController.previousBackStackEntry?.savedStateHandle?.get<String?>("vehicle_ideal_number")
    val rideType =
        navHostController.previousBackStackEntry?.savedStateHandle?.get<String?>("ride_type")


    val cameraPermission = rememberPermissionsState(
        permissions = listOf(Manifest.permission.CAMERA),
        onGrantedAction = {
            hasPermission = true
        },
        onDeniedAction = {
            hasPermission = false
        },
        onPermanentlyDeniedAction = {
            hasPermission = false
        }
    )

    LaunchedEffect(Unit) {
        cameraPermission.launchPermissionRequestsAndAction()
    }

    // Clean up camera on dispose
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }




    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TopNavigationBar(
                    title = "",
                    onBackPress = {
                        navHostController.popBackStack()
                    }
                )

                if (hasPermission && camera?.cameraInfo?.hasFlashUnit() == true) {
                    IconButton(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .align(Alignment.CenterEnd),
                        onClick = {
                            flashEnabled = !flashEnabled
                            camera?.cameraControl?.enableTorch(flashEnabled)
                        }) {
                        Image(
                            painter = if (flashEnabled) painterResource(R.drawable.ic_flash_on) else painterResource(
                                R.drawable.ic_flash_off
                            ),
                            contentDescription = if (flashEnabled) "Flash On" else "Flash Off",
                            modifier = Modifier
                                .size(20.dp),
                            colorFilter = ColorFilter.tint(MyColors.clr_00BCF1_100)
                        )
                    }
                }
            }

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

        ) {


            when {
                hasPermission -> {


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Spacer(modifier = Modifier.height(36.dp))

                        Box(
                            modifier = Modifier
                                .size(260.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(260.dp)
//                                    .fillMaxSize()
                                    .align(Alignment.Center)
                            ) {
                                // Camera Preview (always visible)
                                AndroidView(
                                    factory = { ctx ->
                                        val view = PreviewView(ctx)
                                        previewView = view

                                        val cameraProviderFuture =
                                            ProcessCameraProvider.getInstance(ctx)

                                        cameraProviderFuture.addListener({
                                            val provider = cameraProviderFuture.get()
                                            cameraProvider = provider

                                            val preview = Preview.Builder().build().also {
                                                it.surfaceProvider = view.surfaceProvider
                                            }

                                            val imageAnalysis = ImageAnalysis.Builder()
                                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                                .build()

                                            val barcodeScanner = BarcodeScanning.getClient()

                                            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                                                val mediaImage = imageProxy.image
                                                if (mediaImage != null && isScanning) {
                                                    val image = InputImage.fromMediaImage(
                                                        mediaImage,
                                                        imageProxy.imageInfo.rotationDegrees
                                                    )

                                                    barcodeScanner.process(image)
                                                        .addOnSuccessListener { barcodes ->
                                                            for (barcode in barcodes) {
                                                                when (barcode.valueType) {
                                                                    Barcode.TYPE_TEXT,
                                                                    Barcode.TYPE_URL,
                                                                    Barcode.TYPE_CONTACT_INFO,
                                                                    Barcode.TYPE_EMAIL,
                                                                    Barcode.TYPE_PHONE,
                                                                    Barcode.TYPE_SMS,
                                                                    Barcode.TYPE_WIFI,
                                                                    Barcode.TYPE_GEO,
                                                                    Barcode.TYPE_CALENDAR_EVENT,
                                                                    Barcode.TYPE_DRIVER_LICENSE,
                                                                    Barcode.TYPE_PRODUCT -> {
                                                                        barcode.rawValue?.let { value ->
                                                                            if (isScanning) {
                                                                                scannedCode = value
                                                                                isScanning = false
                                                                                // Stop camera
                                                                                provider.unbindAll()

                                                                                Log.d("QRScanner", "Scanned: $value")
                                                                                navHostController.previousBackStackEntry?.savedStateHandle?.set("is_scan_qr_code", true)
                                                                                navHostController.popBackStack()
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        .addOnCompleteListener {
                                                            imageProxy.close()
                                                        }
                                                } else {
                                                    imageProxy.close()
                                                }
                                            }

                                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                                            try {
                                                provider.unbindAll()
                                                camera = provider.bindToLifecycle(
                                                    lifecycleOwner,
                                                    cameraSelector,
                                                    preview,
                                                    imageAnalysis
                                                )
                                            } catch (e: Exception) {
                                                Log.e("QRScanner", "Camera binding failed", e)
                                            }
                                        }, ContextCompat.getMainExecutor(ctx))

                                        view
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clipToBounds()
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .align(Alignment.Center)
                                )
                            }


                            Image(
                                painter = painterResource(R.drawable.ic_qr_border),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .width(26.dp)
                                    .height(50.dp)
                                    .align(Alignment.TopStart)
                            )

                            Image(
                                painter = painterResource(R.drawable.ic_qr_border),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .padding(end = 20.dp)
                                    .width(26.dp)
                                    .height(50.dp)
                                    .rotate(90f)
                                    .align(Alignment.TopEnd)
                            )

                            Image(
                                painter = painterResource(R.drawable.ic_qr_border),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .width(26.dp)
                                    .height(50.dp)
                                    .rotate(270f)
                                    .align(Alignment.BottomStart)
                            )

                            Image(
                                painter = painterResource(R.drawable.ic_qr_border),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .padding(end = 20.dp)
                                    .width(26.dp)
                                    .height(50.dp)
                                    .rotate(180f)
                                    .align(Alignment.BottomEnd)
                            )
                        }


                        Spacer(modifier = Modifier.height(32.dp))

                        // Overlay with instructions or scanned result
                        if (!isScanning) {
                            // Bottom button
                            FilledButtonGradient(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                text = "Scan Again"
                            ) {
                                isScanning = true
                                scannedCode = null
                                flashEnabled = false

                                // Restart camera
                                cameraProvider?.let { provider ->
                                    previewView?.let { view ->
                                        val preview = Preview.Builder().build().also {
                                            it.surfaceProvider = view.surfaceProvider
                                        }

                                        val imageAnalysis = ImageAnalysis.Builder()
                                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                            .build()

                                        val barcodeScanner = BarcodeScanning.getClient()

                                        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                                            val mediaImage = imageProxy.image
                                            if (mediaImage != null && isScanning) {
                                                val image = InputImage.fromMediaImage(
                                                    mediaImage,
                                                    imageProxy.imageInfo.rotationDegrees
                                                )

                                                barcodeScanner.process(image)
                                                    .addOnSuccessListener { barcodes ->
                                                        for (barcode in barcodes) {
                                                            when (barcode.valueType) {
                                                                Barcode.TYPE_TEXT,
                                                                Barcode.TYPE_URL,
                                                                Barcode.TYPE_CONTACT_INFO,
                                                                Barcode.TYPE_EMAIL,
                                                                Barcode.TYPE_PHONE,
                                                                Barcode.TYPE_SMS,
                                                                Barcode.TYPE_WIFI,
                                                                Barcode.TYPE_GEO,
                                                                Barcode.TYPE_CALENDAR_EVENT,
                                                                Barcode.TYPE_DRIVER_LICENSE,
                                                                Barcode.TYPE_PRODUCT -> {
                                                                    barcode.rawValue?.let { value ->
                                                                        if (isScanning) {
                                                                            scannedCode = value
                                                                            isScanning = false
                                                                            // Stop camera
                                                                            provider.unbindAll()
                                                                            Log.d("QRScanner", "Scanned: $value")
                                                                            navHostController.previousBackStackEntry?.savedStateHandle?.set("is_scan_qr_code", true)
                                                                            navHostController.popBackStack()
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    .addOnCompleteListener {
                                                        imageProxy.close()
                                                    }
                                            } else {
                                                imageProxy.close()
                                            }
                                        }

                                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                                        try {
                                            provider.unbindAll()
                                            camera = provider.bindToLifecycle(
                                                lifecycleOwner,
                                                cameraSelector,
                                                preview,
                                                imageAnalysis
                                            )
                                        } catch (e: Exception) {
                                            Log.e("QRScanner", "Camera restart failed", e)
                                        }
                                    }
                                }
                            }
                        }

                    }


                }

                else -> {
                    // Permission denied
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MyColors.clr_white_100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Camera permission is required to scan QR codes",
                            fontSize = 16.sp,
                            color = MyColors.clr_607080_100,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }
        }
    }


}

