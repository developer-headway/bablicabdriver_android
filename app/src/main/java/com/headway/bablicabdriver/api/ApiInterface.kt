package com.headway.bablicabdriver.api


import com.headway.bablicabdriver.model.commondataclass.CommonRequest
import com.headway.bablicabdriver.model.commondataclass.CommonResponse
import com.headway.bablicabdriver.model.commondataclass.DeviceUIdRequest
import com.headway.bablicabdriver.model.login.SendOtpRequest
import com.headway.bablicabdriver.model.login.SendOtpResponse
import com.headway.bablicabdriver.model.login.VerifyOtpRequest
import com.headway.bablicabdriver.model.login.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST(SEND_OTP)
    suspend fun callSendOtpApi(
        @Body request: SendOtpRequest
    ): Response<SendOtpResponse>

    @POST(VERIFY_OTP)
    suspend fun callVerifyOtpApi(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST(LOGOUT)
    suspend fun callLogoutApi(
        @Header("Authorization") token: String,
        @Body request: DeviceUIdRequest
    ): Response<CommonResponse>

    @POST(DELETE_ACCOUNT)
    suspend fun callDeleteAccountApi(
        @Header("Authorization") token: String
    ): Response<CommonResponse>



    /*

        @GET(PLACE_SEARCH)
        suspend fun callPlaceSearchApi(
            @Query("key") token: String,
            @Query("query") query : String
        ): Response<PlaceSearchResponse>

        @POST(COMPUTE_ROUTE)
        suspend fun callComputeRoutesApi(
            @Header("X-Goog-FieldMask") fieldMask: String = " routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline,routes.routeToken",
            @Header("X-Goog-Api-Key") token: String,
            @Body request: ComputeRoutesRequest
        ): Response<ComputeRoutesResponse>
    */


}

