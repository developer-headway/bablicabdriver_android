package com.headway.bablicabdriver.model.login

data class LoginRequest(
    val email: String = "",
    val password: String = "",
    val user_type: String = "user",
)

data class LoginResponse(
    val data: LoginData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class LoginData(
    val cat_id: String,
    val email: String,
    val id: Int,
    val image_path: String,
    val token: String,
    val u_type: String,
    val username: String
)







