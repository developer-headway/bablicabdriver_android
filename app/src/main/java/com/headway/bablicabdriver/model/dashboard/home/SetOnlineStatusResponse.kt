package com.headway.bablicabdriver.model.dashboard.home


data class SetOnlineStatusRequest(
    val is_online: Boolean
)

data class SetOnlineStatusResponse(
    val data: SetOnlineStatusData,
    val message: String,
    val status: Boolean
)

data class SetOnlineStatusData(
    val is_online: Boolean
)

