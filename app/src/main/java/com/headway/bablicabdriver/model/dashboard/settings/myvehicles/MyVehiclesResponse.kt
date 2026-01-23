package com.headway.bablicabdriver.model.dashboard.settings.myvehicles

data class MyVehiclesResponse(
    val data: List<MyVehiclesData?>?,
    val message: String,
    val status: Boolean
)


data class VehicleDetailRequest(
    val vehicle_id: String
)


data class VehicleDetailResponse(
    val data: MyVehiclesData,
    val message: String,
    val status: Boolean
)

data class MyVehiclesData(
    val assigned_driver_for_this_vehicle: List<AssignedDriverForThisVehicle?>?,
    val babli_vehicle_code: String,
    val rc_book_back_side_img_url: String,
    val rc_book_front_side_img_url: String,
    val vehicle_number: String,
    val vehicle_owner_name: String,
    val vehicle_owner_number: String,
    val vehicle_owner_photo: String,

    val vehicle_current_shift: String,
    val vehicle_id: String
)

data class AssignedDriverForThisVehicle(
    val driver_id: String,
    val driver_image: String,
    val driver_name: String,
    val driver_shift: String,
    val vehicle_owner_name: String,
    val vehicle_owner_number: String,
    val vehicle_owner_photo: String
)