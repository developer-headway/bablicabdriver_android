package com.headway.bablicabdriver.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm_ss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".png",
        externalCacheDir
    )
}