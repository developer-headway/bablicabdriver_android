package com.headway.bablicabdriver.model.dashboard.notifocation

import com.headway.bablicabdriver.model.commondataclass.Pagination

data class NotificationsResponse(
    val data: NotificationsData,
    val message: String,
    val status: Boolean
)

data class NotificationsData(
    val list: List<Notifications?>?,
    val pagination: Pagination?
)

data class Notifications(
    val date_time: String,
    val message: String,
    val title: String
)
data class NotificationsRequest(
    val page: String = "1",
)

