package com.headway.bablicabdriver.res.preferenceManage

import android.content.Context
import com.headway.bablicabdriver.model.login.VerifyOtpData
import com.headway.bablicabdriver.model.registration.profile.ProfileData

class SharedPreferenceManager(private val context: Context){
    private val sharedPreferenceManager = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val introPreferenceManager = context.getSharedPreferences("intro_preferences", Context.MODE_PRIVATE)


    fun storeShowIntro() {
        with(introPreferenceManager.edit()) {
            putBoolean("show_intro", false)
            apply()
        }
    }


    fun getShowIntro(): Boolean {
        return introPreferenceManager.getBoolean("show_intro", true)
    }


    fun storeToken(
        token : String
    ){
        with(sharedPreferenceManager.edit()){
            putString("token", token)
            apply()
        }
    }



//    fun storeSelCompanyData(data: CompanyListResponseData, isBusinessLogin:Boolean = true) {
//        val jsonString = Gson().toJson(data)
//        with(sharedPreferenceManager.edit()) {
//            putString("sel_company_data", jsonString)
//            putBoolean("is_business_login", isBusinessLogin)
//            apply()
//        }
//    }
//
//
//    fun getCompanyData(): CompanyListResponseData? {
//        val jsonString = sharedPreferenceManager.getString("sel_company_data", null)
//        return if (jsonString != null) {
//            Gson().fromJson(jsonString, CompanyListResponseData::class.java)
//        } else {
//            null
//        }
//    }

    fun getToken(): String{
        return sharedPreferenceManager.getString("token", "") ?: ""
    }

    fun getFirstName(): String{
        return sharedPreferenceManager.getString("first_name", "") ?: ""
    }
    fun getLastName(): String{
        return sharedPreferenceManager.getString("last_name", "") ?: ""
    }

    fun getProfilePhoto(): String{
        return sharedPreferenceManager.getString("profile_photo", "") ?: ""
    }


    fun getEmail(): String{
        return sharedPreferenceManager.getString("email", "") ?: ""
    }
    fun getUserId(): String{
        return sharedPreferenceManager.getString("user_id", "") ?: ""
    }


    fun getMobile(): String{
        return sharedPreferenceManager.getString("phone_number", "") ?: ""
    }
    fun getIsLogin(): Boolean {
        return sharedPreferenceManager.getBoolean("is_login",false)
    }



    fun storeVerifyOtpData(
        data : VerifyOtpData
    ){
        with(sharedPreferenceManager.edit()){
            putString("user_id", data.user_id)
            putString("token", data.token)
            putString("first_name", data.first_name)
            putString("last_name", data.last_name)
            putString("email", data.email)
            putString("phone_number", data.phone_number)
            putString("profile_photo", data.profile_photo)
            apply()
        }
    }

    fun storeProfileData(
        data : ProfileData
    ){
        with(sharedPreferenceManager.edit()){
            putString("first_name", data.first_name)
            putString("last_name", data.last_name)
            putString("email", data.email)
            putString("profile_photo", data.profile_photo)
            apply()
        }
    }



    fun setIsLogin(
        isLogin : Boolean
    ){
        with(sharedPreferenceManager.edit()){
            putBoolean("is_login", isLogin)
            apply()
        }
    }

    fun clearPreferences(){
        sharedPreferenceManager.edit().clear().apply()
    }


}


