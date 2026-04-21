package com.headway.bablicabdriver.model.dashboard.settings.refreshment

data class NearbyStoresResponse(
    val status: Boolean? = null,
    val message: String? = null,
    val data: NearbyStoresData? = null
)

data class NearbyStoresData(
    val stores: List<StoreData>? = null,
    val count: Int? = null
)

data class StoreData(
    val id: String? = null,
    val storeId: String? = null,
    val store_name: String? = null,
    val owner_name: String? = null,
    val mobile: String? = null,
    val address: String? = null,
    val city: String? = null,
    val pincode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
