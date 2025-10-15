package com.headway.bablicabdriver.api


import coil.request.ImageRequest
import com.headway.bablicabdriver.model.commondataclass.CommonRequest
import com.headway.bablicabdriver.model.commondataclass.CommonResponse
import com.headway.bablicabdriver.model.commondataclass.DeviceUIdRequest
import com.headway.bablicabdriver.model.login.SendOtpRequest
import com.headway.bablicabdriver.model.login.SendOtpResponse
import com.headway.bablicabdriver.model.login.VerifyOtpRequest
import com.headway.bablicabdriver.model.login.VerifyOtpResponse
import com.headway.bablicabdriver.model.registration.RegistrationDetailsResponse
import com.headway.bablicabdriver.model.registration.UploadDocumentResponse
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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


    @POST(REGISTRATION_DETAIL)
    suspend fun callRegistrationDetailsApi(
        @Header("Authorization") token: String
    ): Response<RegistrationDetailsResponse>




    @Multipart
    @POST(UPLOAD_DOCUMENT)
    suspend fun callUploadDocumentApi(
        @Header("Authorization") token: String,
        @Part("vehicle_number") vehicle_number: RequestBody? = null,
        @Part RCbook_back_photo: MultipartBody.Part? = null,
        @Part RCbook_front_photo: MultipartBody.Part? = null,

//        @Part licence_front_photo: MultipartBody.Part? = null,
//        @Part licence_back_photo: MultipartBody.Part? = null,
//        @Part("dl_number") dl_number: RequestBody? = null,
//
//        @Part aadhar_front_photo: MultipartBody.Part? = null,
//        @Part aadhar_back_photo: MultipartBody.Part? = null,
//        @Part("aadhar_number") aadhar_number: RequestBody? = null,
//
//        @Part pan_photo: MultipartBody.Part? = null,
//        @Part("pan_number") pan_number: RequestBody? = null,
//
//        @Part police_verification_photo: MultipartBody.Part? = null,
    ): Response<UploadDocumentResponse>

    @Multipart
    @POST(UPLOAD_DOCUMENT)
    suspend fun callUploadLicenceApi(
        @Header("Authorization") token: String,
        @Part licence_front_photo: MultipartBody.Part? = null,
        @Part licence_back_photo: MultipartBody.Part? = null,
        @Part("dl_number") dl_number: RequestBody? = null,
    ): Response<UploadDocumentResponse>

    @Multipart
    @POST(UPLOAD_DOCUMENT)
    suspend fun callUploadAadharApi(
        @Header("Authorization") token: String,
        @Part aadhar_front_photo: MultipartBody.Part? = null,
        @Part aadhar_back_photo: MultipartBody.Part? = null,
        @Part("aadhar_number") aadhar_number: RequestBody? = null,
    ): Response<UploadDocumentResponse>

    @Multipart
    @POST(UPLOAD_DOCUMENT)
    suspend fun callUploadPanApi(
        @Header("Authorization") token: String,
        @Part pan_photo: MultipartBody.Part? = null,
        @Part("pan_number") pan_number: RequestBody? = null
    ): Response<UploadDocumentResponse>


    @Multipart
    @POST(UPLOAD_DOCUMENT)
    suspend fun callUploadPoliceVerificationApi(
        @Header("Authorization") token: String,
        @Part police_verification_photo: MultipartBody.Part? = null,
    ): Response<UploadDocumentResponse>


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

