package com.headway.bablicabdriver.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.net.toUri
import com.headway.bablicabdriver.MainActivity
import com.headway.bablicabdriver.res.preferenceManage.SharedPreferenceManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

object AppUtils {
    val currency = "₹"
    val profileImgae = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6Hb5xzFZJCTW4cMqmPwsgfw-gILUV7QevvQ&s"
    val bannerImgae = "https://img.freepik.com/free-psd/horizontal-banner-template-fashion-shopping-store_23-2148786821.jpg"

    val calendarList = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun makePhoneCall(context: Context,phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }
        context.startActivity(intent)
    }
    fun openGoogleMap(context: Context, latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")

        // Check if Google Maps is installed
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Google Maps not installed", Toast.LENGTH_SHORT).show()
        }
    }
    val emailRegex = Pattern.compile(
        "[a-z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-z0-9][a-z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-z0-9][a-z0-9\\-]{0,25}" +
                ")+"
    )


    fun showToast(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }



    @OptIn(DelicateCoroutinesApi::class)
    fun showToastBottom(showBottomToast: MutableState<Boolean>){
        try {
            GlobalScope.launch(Dispatchers.Main) {
                if (!showBottomToast.value){
                    showBottomToast.value = true
                    delay(3000)
                    showBottomToast.value = false
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    fun sendBroadcast(context: Context,message:String) {
        val broadcastIntent = Intent()
        broadcastIntent.putExtra("message",message)
        broadcastIntent.action = "com.notyUpdate.noty"
        context.sendBroadcast(broadcastIntent)
    }

    fun convertDateFormat(dateStr: String, inputFormatStr: String = "dd MMM yyyy", outputFormatStr: String = "yyyy-MM-dd"): String? {
        return try {
            val inputFormat = SimpleDateFormat(inputFormatStr, Locale.ENGLISH)
            val outputFormat = SimpleDateFormat(outputFormatStr, Locale.ENGLISH)
            val date = inputFormat.parse(dateStr)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun formatTime(time: String, inputFormat: String = "HH:mm", outputFormat: String = "hh:mm a"): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern(inputFormat, Locale.getDefault())
            // Use Locale.US to ensure AM/PM is in uppercase
            val outputFormatter = DateTimeFormatter.ofPattern(outputFormat, Locale.US)
            val parsedTime = LocalTime.parse(time, inputFormatter)
            parsedTime.format(outputFormatter)
        } catch (e: Exception) {
            ""
        }
    }



    fun getCurrentDate(format: String):String? {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        val currentDate = formatter.format(calendar.time)
        println("Current date: $currentDate")

        return  currentDate
    }

    fun logoutAndClearData(context: Context) {
        SharedPreferenceManager(context).clearPreferences()
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }
    fun parseTime(time: String): LocalTime {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
    }




}

