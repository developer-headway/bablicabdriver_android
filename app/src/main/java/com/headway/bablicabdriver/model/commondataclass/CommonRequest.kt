package com.headway.bablicabdriver.model.commondataclass

data class CommonRequest(
    val user_id: String,
)
data class CommonResponse(
    val message: String?,
    val status: Boolean?
)

data class DeviceUIdRequest(
    val device_uid: String,
)










