package com.headway.bablicabdriver.screen.registration.rcBookDetails

import android.Manifest
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.headway.bablicabdriver.R
import com.headway.bablicabdriver.api.ErrorsData
import com.headway.bablicabdriver.api.NetWorkFail
import com.headway.bablicabdriver.model.registration.UploadDocumentRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.dialog.CameraOrGallerySelector
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.dialog.goToSettingsDialog
import com.headway.bablicabdriver.res.components.dialog.permissionDeniedDialog
import com.headway.bablicabdriver.res.components.textfields.FilledTextField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.components.toast.ToastExpandHorizontal
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.res.routes.Routes
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.createImageFile
import com.headway.bablicabdriver.utils.dashedBorder
import com.headway.bablicabdriver.utils.permissionhandler.goToSettings
import com.headway.bablicabdriver.utils.permissionhandler.rememberPermissionsState
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.registration.UploadDocumentVm
import java.util.Objects

@Composable
fun RCBookDetailsScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val vehicleNumber = rememberTextFieldState()

    var vehicleNumberError by rememberSaveable {
        mutableStateOf(false)
    }

    val type = navHostController.previousBackStackEntry?.savedStateHandle?.get("type")?:""
    val title = when(type) {
        "RC" -> R.string.rc_book_details
        "DL" -> R.string.driving_license_details
        "aadhar" -> R.string.aadhar_details
        "pan" -> R.string.pan_details
        "police_verification" -> R.string.police_varification
        else -> R.string.rc_book_details
    }

    ////////////////////////
    ////////////////////////

    var cameraUri by remember {
        mutableStateOf<Uri> (Uri.EMPTY)
    }

    val showGalleryOrImagePicker = remember {
        mutableStateOf(false)
    }
    var frontImageUri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }
    var backImageUri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }
    var isFrontImage by rememberSaveable {
        mutableStateOf(false)
    }
    var frontImageError by rememberSaveable {
        mutableStateOf(false)
    }

    var backImageError by rememberSaveable {
        mutableStateOf(false)
    }

    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.uriContent != null){
            result.uriContent?.let { uri ->
                showGalleryOrImagePicker.value = false

                if (isFrontImage) {
                    frontImageUri = uri
                } else {
                    backImageUri = uri
                }

            }
        }
    }
    fun handleUriInput(uri: Uri?){
        cropImage.launch(
            CropImageContractOptions(
                uri = uri,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeCamera = false,
                    imageSourceIncludeGallery = true,
                    autoZoomEnabled = true,
                    allowFlipping = false,
                    centerMoveEnabled = true,
                    multiTouchEnabled = false,
                    snapRadius = 0f,
                    initialCropWindowPaddingRatio = 0f,
                    scaleType = CropImageView.ScaleType.FIT_CENTER,
                    outputCompressQuality = 40
                )
            ),
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            if (it) {
                handleUriInput(cameraUri)
            }
        }
    )
    val cameraPermission = rememberPermissionsState(permissions = listOf(
        Manifest.permission.CAMERA
    ),
        onGrantedAction = {
            val file = context.createImageFile()
            cameraUri = FileProvider.getUriForFile(
                Objects.requireNonNull(context),
                context.applicationContext.packageName + ".provider", file
            )

            cameraLauncher.launch(cameraUri)
        },
        onDeniedAction = {
            permissionDeniedDialog(context) {}
        },
        onPermanentlyDeniedAction = {
            goToSettingsDialog(context) {
                context.goToSettings(it)
            }
        }
    )

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { it?.let { uri -> handleUriInput(uri) } }


    /////////////////////////////////////////////
    /////////////////////////////////////////////
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }


    val uploadDocumentVm : UploadDocumentVm = viewModel()
    fun callUploadDocumentApi() {
        if (AppUtils.isInternetAvailable(context)) {
            val request = UploadDocumentRequest(
                RCbook_front_photo = frontImageUri,
                RCbook_back_photo = backImageUri,

                licence_front_photo = frontImageUri,
                licence_back_photo = backImageUri,

                aadhar_front_photo = frontImageUri,
                aadhar_back_photo = backImageUri,

                pan_photo = frontImageUri,

                police_verification_photo = frontImageUri,

                vehicle_number = vehicleNumber.text.trim().toString(),
                dl_number = vehicleNumber.text.trim().toString(),
                aadhar_number = vehicleNumber.text.trim().toString(),
                pan_number = vehicleNumber.text.trim().toString()
            )
            uploadDocumentVm.callUploadDocumentApi(
                application = activity?.application,
                type = type,
                token = sharedPreferenceManager.getToken(),
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it?:""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = {response->
                    if (response?.status == true) {
                        navHostController.previousBackStackEntry?.savedStateHandle?.set("refresh",true)
                        navHostController.popBackStack()
                    } else {
                        errorStates.bottomToastText.value = response?.message?:""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }
                }
            )
        } else {
            errorStates.showInternetError.value = true
            networkError = NetWorkFail.NetworkError.ordinal
        }
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////




    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MyColors.clr_white_100,
                    )
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ToastExpandHorizontal(
                    showBottomToast = errorStates.showBottomToast,
                    s = errorStates.bottomToastText.value
                )
                FilledButtonGradient(
                    text = stringResource(R.string.submit),
                    textColor = MyColors.clr_white_100,
                    onClick = {


                        frontImageError = frontImageUri==Uri.EMPTY


                        when (type) {

                            "police_verification"->{

                            }
                            "pan"-> {
                                vehicleNumberError = vehicleNumber.text.trim().isEmpty()
                            }
                            else -> {
                                vehicleNumberError = vehicleNumber.text.trim().isEmpty()
                                backImageError = backImageUri==Uri.EMPTY
                            }
                        }

                        if (frontImageError || backImageError) {
                            errorStates.bottomToastText.value = if (frontImageError) "Select RC Front Side Photo" else "Select RC Back Side Photo"
                            AppUtils.showToastBottom(errorStates.showBottomToast)
                        }
                        if (!vehicleNumberError && !frontImageError && !backImageError) {
                            callUploadDocumentApi()
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )

                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )
            }
        },
        topBar = {


            TopNavigationBar(
                title = stringResource(title),
                onBackPress = {
                    navHostController.popBackStack()
                }
            )
        },
        containerColor = MyColors.clr_white_100
    ) { innerPadding ->


        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_circle_check),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(22.dp)
                    )

                    Spacer(
                        modifier = Modifier
                            .width(12.dp)
                    )

                    TextView(
                        text = stringResource(R.string.the_photo_and),
                        textColor = MyColors.clr_4B4B4B_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 14.sp
                    )

                }

                Spacer(
                    modifier = Modifier
                        .height(6.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_circle_check),
                        contentDescription = stringResource(R.string.img_des),
                        modifier = Modifier
                            .size(22.dp)
                    )

                    Spacer(
                        modifier = Modifier
                            .width(12.dp)
                    )

                    TextView(
                        text = stringResource(R.string.only_document),
                        textColor = MyColors.clr_4B4B4B_100,
                        fontFamily = MyFonts.fontRegular,
                        fontSize = 14.sp
                    )

                }
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    color = MyColors.clr_00BCF1_100
                )
                Spacer(
                    modifier = Modifier
                        .height(22.dp)
                )


                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )


                when (type) {
                    "DL", "aadhar", "RC", "pan" -> {

                        val title = when(type) {
                            "RC" -> R.string.vehicle_number
                            "DL" -> R.string.driving_license_number
                            "aadhar" -> R.string.aadhar_number
                            "pan" -> R.string.pan_number
                            else -> R.string.vehicle_number
                        }
                        TextView(
                            text = stringResource(title),
                            textColor = MyColors.clr_607080_100,
                            fontSize = 14.sp,
                            fontFamily = MyFonts.fontRegular,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                        )

                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )


                        FilledTextField(
                            state = vehicleNumber,
                            placeHolder = stringResource(R.string.enter_number),
                            isTyping = {
                                vehicleNumberError = false
                            },
                            borderColor = MyColors.clr_E8E8E8_100,
                            modifier = Modifier
                                .padding(horizontal = 20.dp),
                            textFontFamily = MyFonts.fontMedium,
                            textColor = MyColors.clr_5A5A5A_100,
                            textFontSize = 14.sp,
                            isLast = true,
                            isTypeNumeric = false
                        )

                        TextView(
                            text =  if(vehicleNumberError) { stringResource(R.string.this_field_can_not_be_empty) } else "",
                            modifier = Modifier
                                .padding(top = 3.dp)
                                .padding(horizontal = 20.dp)
                                .height(18.dp),
                            fontSize = 10.sp,
                            fontFamily = MyFonts.fontRegular,
                            textColor = MyColors.clr_FA4949_100
                        )
                    }
                }




                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier

                            .weight(1f)
                    ) {

                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                        )
                        TextView(
                            text = stringResource(R.string.attach_front_side_photo),
                            textColor = MyColors.clr_607080_100,
                            fontSize = 12.sp,
                            fontFamily = MyFonts.fontSemiBold,
                            modifier = Modifier
                        )

                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {


                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .dashedBorder(
                                        width = 1.dp,
                                        color = MyColors.clr_00BCF1_100,
                                        radius = 12.dp
                                    )
                                    .clickable {
                                        isFrontImage = true
                                        showGalleryOrImagePicker.value = true
                                    }
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {

                                if(frontImageUri== Uri.EMPTY) {
                                    Column (
                                        modifier = Modifier,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Image(
                                            painter = painterResource(R.drawable.ic_upload_icon),
                                            contentDescription = stringResource(R.string.img_des),
                                            modifier = Modifier
                                                .size(50.dp)
                                        )

                                        TextView(
                                            text = stringResource(R.string.upload_photo),
                                            textColor = MyColors.clr_00BCF1_100,
                                            fontFamily = MyFonts.fontRegular,
                                            fontSize = 12.sp
                                        )
                                    }
                                } else {
                                    var imgLoading by remember {
                                        mutableStateOf(true)
                                    }
                                    AsyncImage(
                                        model = ImageRequest
                                            .Builder(context)
                                            .data(
                                                frontImageUri
                                            )
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .shimmerEffect(imgLoading),
                                        contentScale = ContentScale.Crop,
                                        onLoading = {
                                            imgLoading = true
                                        },
                                        onSuccess = {
                                            imgLoading = false
                                        }
                                    )
                                }
                            }

                            if (frontImageUri != Uri.EMPTY) {
                                Box(
                                    modifier = Modifier
                                        .offset(y = (-10).dp, x = (10).dp)
                                        .size(24.dp)
                                        .align(Alignment.TopEnd)
                                        .clickable {
                                            frontImageUri = Uri.EMPTY
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_remove),
                                        contentDescription = stringResource(R.string.img_des),
                                        modifier = Modifier
                                            .size(18.dp)
                                    )
                                }
                            }


                        }

                    }

                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )

                    if (type == "pan" || type == "police_verification") {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {}
                    } else {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            Spacer(
                                modifier = Modifier
                                    .height(12.dp)
                            )
                            TextView(
                                text = stringResource(R.string.attach_back_side_photo),
                                textColor = MyColors.clr_607080_100,
                                fontSize = 12.sp,
                                fontFamily = MyFonts.fontSemiBold,
                                modifier = Modifier

                            )

                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {


                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .dashedBorder(
                                            width = 1.dp,
                                            color = MyColors.clr_00BCF1_100,
                                            radius = 12.dp
                                        )
                                        .clickable {
                                            isFrontImage = false
                                            showGalleryOrImagePicker.value = true
                                        }
                                        .padding(2.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    if(backImageUri== Uri.EMPTY) {
                                        Column (
                                            modifier = Modifier,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            Image(
                                                painter = painterResource(R.drawable.ic_upload_icon),
                                                contentDescription = stringResource(R.string.img_des),
                                                modifier = Modifier
                                                    .size(50.dp)
                                            )

                                            TextView(
                                                text = stringResource(R.string.upload_photo),
                                                textColor = MyColors.clr_00BCF1_100,
                                                fontFamily = MyFonts.fontRegular,
                                                fontSize = 12.sp
                                            )
                                        }
                                    } else {
                                        var imgLoading by remember {
                                            mutableStateOf(true)
                                        }
                                        AsyncImage(
                                            model = ImageRequest
                                                .Builder(context)
                                                .data(
                                                    backImageUri
                                                )
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(shape = RoundedCornerShape(12.dp))
                                                .shimmerEffect(imgLoading),
                                            contentScale = ContentScale.Crop,
                                            onLoading = {
                                                imgLoading = true
                                            },
                                            onSuccess = {
                                                imgLoading = false
                                            }
                                        )
                                    }
                                }


                                if (backImageUri != Uri.EMPTY) {
                                    Box(
                                        modifier = Modifier
                                            .offset(y = (-10).dp, x = (10).dp)
                                            .size(24.dp)
                                            .align(Alignment.TopEnd)
                                            .clickable {
                                                backImageUri = Uri.EMPTY
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.ic_remove),
                                            contentDescription = stringResource(R.string.img_des),
                                            modifier = Modifier
                                                .size(18.dp)
                                        )
                                    }
                                }


                            }

                        }
                    }




                }





            }

        }

    }



    if (showGalleryOrImagePicker.value) {
        CameraOrGallerySelector(
            visible = showGalleryOrImagePicker,
            onCamera = {
                cameraPermission.launchPermissionRequestsAndAction()
            },
            onGallery = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
    }


    CommonErrorDialogs(errorStates, false) {
        if (networkError== NetWorkFail.NetworkError.ordinal) {
            networkError = NetWorkFail.NoError.ordinal
            errorStates.showInternetError.value = false
            callUploadDocumentApi()
        }
    }
    if (uploadDocumentVm._isLoading.collectAsState().value) {
        Loader()
    }





}