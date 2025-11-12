package com.headway.bablicabdriver.utils

import android.app.Application
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.UUID
import kotlin.io.copyTo
import kotlin.let
import kotlin.text.filter
import kotlin.text.isEmpty
import kotlin.text.isLetterOrDigit

fun createImgBodyPart(
    context: Application?,
    uri: Uri?,
    label: String,
    isPdf: Boolean = false,
    name : String = ""
): MultipartBody.Part? {
    if (uri == null || uri == Uri.EMPTY) {
        return null
    }
    try {
        val filesDir = context?.externalCacheDir
        val timestamp = if (name.isEmpty()){
            UUID.randomUUID().toString().filter { it.isLetterOrDigit() }
        }else{
            "$name${UUID.randomUUID().toString().filter { it.isLetterOrDigit() }}"
        }
        val file = File(filesDir, if (isPdf) "$timestamp.pdf" else "$timestamp.jpg")
        val outputStream = FileOutputStream(file)

        uri.let {
            context?.contentResolver?.openInputStream(
                it
            )
        }?.copyTo(outputStream)
        val requestBody =
            file.asRequestBody(if (isPdf) "pdf/*".toMediaTypeOrNull() else "image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(label, file.name, requestBody)
    }catch (e : Exception){
        e.printStackTrace()
        return null
    }
}


