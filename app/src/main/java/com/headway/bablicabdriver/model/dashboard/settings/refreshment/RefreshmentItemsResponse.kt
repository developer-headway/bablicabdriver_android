package com.headway.bablicabdriver.model.dashboard.settings.refreshment

data class RefreshmentItemsResponse(
    val status: Boolean? = null,
    val message: String? = null,
    val data: RefreshmentItemsData? = null
)

data class RefreshmentItemsData(
    val driver_id: String? = null,
    val driver_name: String? = null,
    val vehicle_info: VehicleInfo? = null,
    val items: List<RefreshmentItemData>? = null
)

data class VehicleInfo(
    val vehicle_id: String? = null,
    val vehicle_number: String? = null
)

data class RefreshmentItemData(
    val item_id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Int? = null,
    val category: String? = null,
    val image: String? = null,
    val stock_quantity: Int? = null,
    val is_in_stock: Boolean? = null
)
