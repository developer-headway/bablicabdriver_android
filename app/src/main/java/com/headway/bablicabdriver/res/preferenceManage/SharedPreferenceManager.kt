package com.headway.bablicabdriver.res.preferenceManage

import android.content.Context
import com.headway.bablicabdriver.model.login.VerifyOtpData

class SharedPreferenceManager(private val context: Context){
    private val sharedPreferenceManager = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val introPreferenceManager = context.getSharedPreferences("intro_preferences", Context.MODE_PRIVATE)


    fun storeShowIntro() {
        with(introPreferenceManager.edit()) {
            putBoolean("show_intro", true)
            apply()
        }
    }


    fun getShowIntro(): Boolean {
        return introPreferenceManager.getBoolean("show_intro", false)
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



    fun getRoleId(): Int{
        return sharedPreferenceManager.getInt("role_id", 0)
    }

    fun getToken(): String{
        return sharedPreferenceManager.getString("token", "") ?: ""
    }
    fun getUserId(): String{
        return sharedPreferenceManager.getString("user_id", "") ?: ""
    }
    fun getSocietyId(): String{
        return sharedPreferenceManager.getString("society_id", "") ?: ""
    }

    fun getSocietyName(): String{
        return sharedPreferenceManager.getString("society_name", "") ?: ""
    }
    fun getUserName(): String{
        return sharedPreferenceManager.getString("user_name", "") ?: ""
    }

    fun getFlatHouseNo(): String{
        return sharedPreferenceManager.getString("flat_houseNo", "") ?: ""
    }
    fun getBlock(): String{
        return sharedPreferenceManager.getString("block", "") ?: ""
    }

    fun getProfilePhoto(): String{
        return sharedPreferenceManager.getString("profile_photo", "") ?: ""
    }


    fun getEmail(): String{
        return sharedPreferenceManager.getString("profile_photo", "") ?: ""
    }
    fun getMobile(): String{
        return sharedPreferenceManager.getString("profile_photo", "") ?: ""
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


