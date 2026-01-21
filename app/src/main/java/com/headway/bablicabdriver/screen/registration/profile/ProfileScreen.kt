package com.headway.bablicabdriver.screen.registration.profile

import android.Manifest
import android.app.DatePickerDialog
import android.net.Uri
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.graphics.toColorInt
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
import com.headway.bablicabdriver.model.registration.profile.UpdateProfileRequest
import com.headway.bablicabdriver.res.Loader
import com.headway.bablicabdriver.res.components.bar.TopNavigationBar
import com.headway.bablicabdriver.res.components.buttons.FilledButtonGradient
import com.headway.bablicabdriver.res.components.dialog.CameraOrGallerySelector
import com.headway.bablicabdriver.res.components.dialog.CommonErrorDialogs
import com.headway.bablicabdriver.res.components.dialog.goToSettingsDialog
import com.headway.bablicabdriver.res.components.dialog.permissionDeniedDialog
import com.headway.bablicabdriver.res.components.textfields.FilledTextField
import com.headway.bablicabdriver.res.components.textview.TextView
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import com.headway.bablicabdriver.ui.theme.MyColors
import com.headway.bablicabdriver.ui.theme.MyFonts
import com.headway.bablicabdriver.utils.AppUtils
import com.headway.bablicabdriver.utils.createImageFile
import com.headway.bablicabdriver.utils.dashedBorder
import com.headway.bablicabdriver.utils.permissionhandler.goToSettings
import com.headway.bablicabdriver.utils.permissionhandler.rememberPermissionsState
import com.headway.bablicabdriver.utils.shimmerEffect
import com.headway.bablicabdriver.viewmodel.MainViewModel
import com.headway.bablicabdriver.viewmodel.registration.profile.UpdateProfileVm
import com.headway.bablicabdriver.viewmodel.settings.ProfileVm
import java.util.Calendar
import java.util.Objects
import java.util.TimeZone
import kotlin.math.min

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val isEdit = navHostController.previousBackStackEntry?.savedStateHandle?.get("is_edit") ?: false



    val firstName = rememberTextFieldState()
    var firstNameError by rememberSaveable {
        mutableStateOf(false)
    }

    val lastName = rememberTextFieldState()
    var lastNameError by rememberSaveable {
        mutableStateOf(false)
    }

    val email = rememberTextFieldState()
    var emailError by rememberSaveable {
        mutableStateOf(false)
    }
    val date = rememberSaveable {
        mutableStateOf("")
    }
    var dateError by rememberSaveable {
        mutableStateOf(false)
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    fun datePicker(selDate: MutableState<String>, onResult: () -> Unit = {}) {
        var month = ""
        var day = ""
        val c = Calendar.getInstance()
        val splitDate = selDate.value.split(" ")
        val mYear = if (selDate.value.isEmpty()) c[Calendar.YEAR] else splitDate[2].toInt()
        val mMonth =
            if (selDate.value.isEmpty()) c[Calendar.MONTH] else AppUtils.calendarList.indexOf(
                splitDate[1]
            )
        val mDay = if (selDate.value.isEmpty()) c[Calendar.DAY_OF_MONTH] else splitDate[0].toInt()

        val datePickerDialog = DatePickerDialog(
            context, R.style.DialogTheme,
            { _, year, monthOfYear1, dayOfMonth ->
                var monthOfYear = monthOfYear1
                monthOfYear += 1
                month = AppUtils.calendarList[monthOfYear - 1]
                day = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                selDate.value = "$day $month $year"
                onResult()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate =
            Calendar.getInstance(TimeZone.getDefault()).timeInMillis
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(android.graphics.Color.GRAY)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor("#FF2096F3".toColorInt())
    }

    /////////////////////////////////////////////
    /////////////////////////////////////////////

    var cameraUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val showGalleryOrImagePicker = remember {
        mutableStateOf(false)
    }
    var imageUri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }


    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.uriContent != null) {
            result.uriContent?.let { uri ->
                showGalleryOrImagePicker.value = false
                imageUri = uri
            }
        }
    }

    fun handleUriInput(uri: Uri?) {
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
    val cameraPermission = rememberPermissionsState(
        permissions = listOf(
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
    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    var profileNetworkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val errorStates by remember {
        mutableStateOf(ErrorsData())
    }
    val profileVm: ProfileVm = viewModel()
    val profileData by profileVm.profileData.collectAsState()

    var profileImageUrl by rememberSaveable {
        mutableStateOf("")
    }


    fun callProfileApi() {
        if (AppUtils.isInternetAvailable(context)) {

            val token = sharedPreferenceManager.getToken()
            profileVm.callProfileApi(
                token = token,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it ?: ""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = { response ->
                    if (response?.status == true) {

                        val data = response.data

                        sharedPreferenceManager.storeProfileData(data)
                        Log.d("msg", "anca-dxsacsadkjcjsadc-dsajcndsaickm")
                        profileImageUrl = data.profile_photo
                        firstName.edit {
                            replace(0, length, data.first_name)
                        }
                        lastName.edit {
                            replace(0, length, data.last_name)
                        }
                        email.edit {
                            replace(0, length, data.email)
                        }
                        date.value =
                            AppUtils.convertDateFormat(data.dob, "yyyy-MM-dd", "dd MMM yyyy") ?: ""
                    } else {
                        errorStates.bottomToastText.value = response?.message ?: ""
                        AppUtils.showToastBottom(errorStates.showBottomToast)
                    }

                }
            )
        } else {
            errorStates.showInternetError.value = true
            profileNetworkError = NetWorkFail.NetworkError.ordinal
        }
    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    var isFirstTime by rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(
        true
    ) {
        if (isFirstTime && isEdit) {
            isFirstTime = false
            callProfileApi()
        }
    }


    /////////////////////////////////////////////
    /////////////////////////////////////////////


    var networkError by rememberSaveable {
        mutableIntStateOf(NetWorkFail.NoError.ordinal)
    }


    val updateProfileVm: UpdateProfileVm = viewModel()
    fun callUpdateProfileApi() {
        if (AppUtils.isInternetAvailable(context)) {

            val dob = AppUtils.convertDateFormat(date.value)
            val request = UpdateProfileRequest(
                first_name = firstName.text.trim().toString(),
                last_name = lastName.text.trim().toString(),
                email = email.text.trim().toString(),
                dob = dob ?: "",
                profile_photo = imageUri
            )
            updateProfileVm.callUpdateProfileApi(
                application = activity?.application,
                token = sharedPreferenceManager.getToken(),
                request = request,
                errorStates = errorStates,
                onError = {
                    errorStates.bottomToastText.value = it ?: ""
                    AppUtils.showToastBottom(errorStates.showBottomToast)
                },
                onSuccess = { response ->
                    if (response?.status == true) {
                        mainViewModel.snackBarText.value = response.message
                        mainViewModel.showSnackBar.value = true
                        navHostController.previousBackStackEntry?.savedStateHandle?.set(
                            "refresh",
                            true
                        )
                        navHostController.popBackStack()
                        sharedPreferenceManager.storeProfileData(response.data)
                    } else {
                        errorStates.bottomToastText.value = response?.message ?: ""
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
                    .imePadding()
            ) {

                FilledButtonGradient(
                    text = stringResource(if (isEdit) R.string.submit else R.string.next),
                    textColor = MyColors.clr_white_100,
                    onClick = {
                        firstNameError = firstName.text.isEmpty()
                        lastNameError = lastName.text.isEmpty()
                        dateError = date.value.isEmpty()
                        if (email.text.isNotEmpty()) {
                            emailError =
                                !AppUtils.emailRegex.matcher(email.text.toString().lowercase())
                                    .matches()
                        }

                        if (!firstNameError && !lastNameError && !emailError && !dateError) {
                            callUpdateProfileApi()
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
                title = stringResource(R.string.profile),
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
                .verticalScroll(rememberScrollState())
        ) {


            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            TextView(
                text = stringResource(R.string.profile),
                textColor = MyColors.clr_132234_100,
                fontSize = 18.sp,
                fontFamily = MyFonts.fontSemiBold,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )
            TextView(
                text = stringResource(R.string.upload_profile_photo),
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

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .size(96.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(shape = CircleShape)
                        .dashedBorder(
                            width = 1.5.dp,
                            color = MyColors.clr_575757_50,
                            radius = 41.dp
                        )
                        .clickable {
                            showGalleryOrImagePicker.value = true
                        }
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {

                    if (imageUri == Uri.EMPTY && !isEdit) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(
                                    color = MyColors.clr_DCDCDC_100,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Image(
                                painter = painterResource(R.drawable.ic_camera),
                                contentDescription = stringResource(R.string.img_des),
                                modifier = Modifier
                                    .size(24.dp),
                                colorFilter = ColorFilter.tint(
                                    color = MyColors.clr_575757_100
                                )
                            )
                        }
                    } else {
                        var imgLoading by remember {
                            mutableStateOf(true)
                        }
                        AsyncImage(
                            model = ImageRequest
                                .Builder(context)
                                .data(if (imageUri != Uri.EMPTY) imageUri else profileData?.profile_photo)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = CircleShape)
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

            }



            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )


            TextView(
                text = stringResource(R.string.first_name),
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
                state = firstName,
                placeHolder = stringResource(R.string.enter_name),
                isTyping = {
                    firstNameError = false
                },
                borderColor = MyColors.clr_E8E8E8_100,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                textFontFamily = MyFonts.fontMedium,
                textColor = MyColors.clr_5A5A5A_100,
                textFontSize = 14.sp,
                isLast = false,
                isTypeNumeric = false
            )

            TextView(
                text = if (firstNameError) {
                    stringResource(R.string.this_field_can_not_be_empty)
                } else "",
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


            TextView(
                text = stringResource(R.string.last_name),
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
                state = lastName,
                placeHolder = stringResource(R.string.enter_name),
                isTyping = {
                    lastNameError = false
                },
                borderColor = MyColors.clr_E8E8E8_100,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                textFontFamily = MyFonts.fontMedium,
                textColor = MyColors.clr_5A5A5A_100,
                textFontSize = 14.sp,
                isLast = false,
                isTypeNumeric = false
            )

            TextView(
                text = if (lastNameError) {
                    stringResource(R.string.this_field_can_not_be_empty)
                } else "",
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

            TextView(
                text = stringResource(R.string.email),
                textColor = MyColors.clr_607080_100,
                fontSize = 14.sp,
                fontFamily = MyFonts.fontRegular,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )



            FilledTextField(
                state = email,
                placeHolder = stringResource(R.string.enter_email),
                isTyping = {
                    emailError = false
                },
                borderColor = MyColors.clr_E8E8E8_100,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                textFontFamily = MyFonts.fontRegular,
                textColor = MyColors.clr_313131_100,
                textFontSize = 14.sp,
                isLast = true,
                isTypeNumeric = false,
                enabled = true,
                keyboardType = KeyboardType.Email
            )


            TextView(
                text = if (emailError) {
                    if (email.text.isNotEmpty()) stringResource(R.string.kindly_enter_valid_email) else stringResource(
                        R.string.this_field_can_not_be_empty
                    )
                } else "",
                modifier = Modifier
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

            TextView(
                text = stringResource(R.string.date_of_birth),
                textColor = MyColors.clr_607080_100,
                fontSize = 14.sp,
                fontFamily = MyFonts.fontRegular,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(44.dp)
                    .border(
                        width = 1.dp,
                        color = MyColors.clr_E8E8E8_100,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        datePicker(date) {
                            dateError = false
                        }
                    }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextView(
                    text = date.value.ifEmpty { stringResource(R.string.date) },
                    fontSize = 14.sp,
                    textColor = MyColors.clr_313131_100,
                    fontFamily = MyFonts.fontRegular,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                )

                Image(
                    painter = painterResource(R.drawable.ic_calendar1),
                    contentDescription = stringResource(R.string.img_des),
                    modifier = Modifier
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(color = if (date.value.isEmpty()) MyColors.clr_707070_100 else MyColors.clr_243369_100)
                )

            }

            TextView(
                text = if (dateError) {
                    stringResource(R.string.this_field_can_not_be_empty)
                } else "",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(18.dp),
                fontSize = 10.sp,
                fontFamily = MyFonts.fontRegular,
                textColor = MyColors.clr_FA4949_100
            )


            Spacer(
                modifier = Modifier
                    .height(50.dp)
            )
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

    CommonErrorDialogs(
        showToast = false,
        errorStates = errorStates,
        onNoInternetRetry = {
            if (networkError == NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                networkError = NetWorkFail.NoError.ordinal
                callUpdateProfileApi()
            }
            if (profileNetworkError == NetWorkFail.NetworkError.ordinal) {
                errorStates.showInternetError.value = false
                profileNetworkError = NetWorkFail.NoError.ordinal
                callProfileApi()
            }
        },
    )


    if (updateProfileVm._isLoading.collectAsState().value || profileVm._isLoading.collectAsState().value) {
        Loader()
    }

}

