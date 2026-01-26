package com.headway.bablicabdriver.api

import com.headway.bablicabdriver.model.commondataclass.CommonResponse
import com.headway.bablicabdriver.model.commondataclass.DeviceUIdRequest
import com.headway.bablicabdriver.model.dashboard.home.AcceptRideRequest
import com.headway.bablicabdriver.model.dashboard.home.AcceptRideResponse
import com.headway.bablicabdriver.model.dashboard.home.ArrivedPickupRequest
import com.headway.bablicabdriver.model.dashboard.home.ArrivedPickupResponse
import com.headway.bablicabdriver.model.dashboard.home.CompleteRideRequest
import com.headway.bablicabdriver.model.dashboard.home.CompleteRideResponse
import com.headway.bablicabdriver.model.dashboard.home.ComputeRoutesRequest
import com.headway.bablicabdriver.model.dashboard.home.ComputeRoutesResponse
import com.headway.bablicabdriver.model.dashboard.home.HomePageResponse
import com.headway.bablicabdriver.model.dashboard.home.RidePaymentRequest
import com.headway.bablicabdriver.model.dashboard.home.RidePaymentResponse
import com.headway.bablicabdriver.model.dashboard.home.SetOnlineStatusRequest
import com.headway.bablicabdriver.model.dashboard.home.SetOnlineStatusResponse
import com.headway.bablicabdriver.model.dashboard.home.SetShuttleRouteRequest
import com.headway.bablicabdriver.model.dashboard.home.SetShuttleRouteResponse
import com.headway.bablicabdriver.model.dashboard.home.StartRideRequest
import com.headway.bablicabdriver.model.dashboard.home.StartRideResponse
import com.headway.bablicabdriver.model.dashboard.home.UpdateDriverLocationRequest
import com.headway.bablicabdriver.model.dashboard.home.UpdateDriverLocationResponse
import com.headway.bablicabdriver.model.dashboard.myride.RideHistoryRequest
import com.headway.bablicabdriver.model.dashboard.myride.RideHistoryResponse
import com.headway.bablicabdriver.model.dashboard.notifocation.NotificationsRequest
import com.headway.bablicabdriver.model.dashboard.notifocation.NotificationsResponse
import com.headway.bablicabdriver.model.dashboard.settings.bankDetails.BankDetailsResponse
import com.headway.bablicabdriver.model.dashboard.settings.bankDetails.UpdateBankDetailsRequest
import com.headway.bablicabdriver.model.dashboard.wallet.WalletTransactionsRequest
import com.headway.bablicabdriver.model.dashboard.wallet.WalletTransactionsResponse
import com.headway.bablicabdriver.model.dashboard.wallet.WithdrawMoneyRequest
import com.headway.bablicabdriver.model.dashboard.wallet.WithdrawMoneyResponse
import com.headway.bablicabdriver.model.login.SendOtpRequest
import com.headway.bablicabdriver.model.login.SendOtpResponse
import com.headway.bablicabdriver.model.login.VerifyOtpRequest
import com.headway.bablicabdriver.model.login.VerifyOtpResponse
import com.headway.bablicabdriver.model.registration.RegistrationDetailsResponse
import com.headway.bablicabdriver.model.registration.UploadDocumentResponse
import com.headway.bablicabdriver.model.registration.profile.ProfileResponse
import com.headway.bablicabdriver.model.registration.profile.UpdateProfileResponse
import com.headway.bablicabdriver.model.dashboard.home.ShuttleRouteResponse
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverDetailsRequest
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.DriverDetailsResponse
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.MyVehiclesResponse
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.VehicleDetailRequest
import com.headway.bablicabdriver.model.dashboard.settings.myvehicles.VehicleDetailResponse
import okhttp3.MultipartBody
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

    @Multipart
    @POST(UPDATE_PROFILE)
    suspend fun callUpdateProfileApi(
        @Header("Authorization") token: String,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part profile_photo: MultipartBody.Part?
    ): Response<UpdateProfileResponse>

    @POST(PROFILE)
    suspend fun callProfileApi(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @POST(HOME_PAGE)
    suspend fun callHomePageApi(
        @Header("Authorization") token: String
    ): Response<HomePageResponse>

    @POST(RIDE_HISTORY)
    suspend fun callRideHistoryApi(
        @Header("Authorization") token: String,
        @Body request: RideHistoryRequest
    ): Response<RideHistoryResponse>

    @POST(SET_ONLINE_STATUS)
    suspend fun callSetOnlineStatusApi(
        @Header("Authorization") token: String,
        @Body request: SetOnlineStatusRequest
    ): Response<SetOnlineStatusResponse>

    @POST(UPDATE_DRIVER_LOCATION)
    suspend fun callUpdateDriverLocationApi(
        @Header("Authorization") token: String,
        @Body request: UpdateDriverLocationRequest
    ): Response<UpdateDriverLocationResponse>


    @POST(ACCEPT_RIDE)
    suspend fun callAcceptRideApi(
        @Header("Authorization") token: String,
        @Body request: AcceptRideRequest
    ): Response<AcceptRideResponse>

    @POST(ARRIVED_PICKUP)
    suspend fun callArrivedPickupApi(
        @Header("Authorization") token: String,
        @Body request: ArrivedPickupRequest
    ): Response<ArrivedPickupResponse>


    @POST(START_RIDE)
    suspend fun callStartRideApi(
        @Header("Authorization") token: String,
        @Body request: StartRideRequest
    ): Response<StartRideResponse>

    @POST(COMPLETE_RIDE)
    suspend fun callCompleteRideApi(
        @Header("Authorization") token: String,
        @Body request: CompleteRideRequest
    ): Response<CompleteRideResponse>

    @POST(CANCEL_RIDE)
    suspend fun callCancelRideApi(
        @Header("Authorization") token: String,
        @Body request: CompleteRideRequest
    ): Response<CommonResponse>

    @POST(RIDE_PAYMENT)
    suspend fun callRidePaymentApi(
        @Header("Authorization") token: String,
        @Body request: RidePaymentRequest
    ): Response<RidePaymentResponse>

    @POST(WALLET_TRANSACTIONS)
    suspend fun callWalletTransactionsApi(
        @Header("Authorization") token: String,
        @Body request: WalletTransactionsRequest
    ): Response<WalletTransactionsResponse>

    @POST(NOTIFICATIONS)
    suspend fun callNotificationsApi(
        @Header("Authorization") token: String,
        @Body request: NotificationsRequest
    ): Response<NotificationsResponse>



    @POST(GET_BANK_DETAILS)
    suspend fun callGetBankDetailsApi(
        @Header("Authorization") token: String
    ): Response<BankDetailsResponse>

    @POST(UPDATE_BANK_DETAILS)
    suspend fun callUpdateBankDetailsApi(
        @Header("Authorization") token: String,
        @Body request: UpdateBankDetailsRequest
    ): Response<BankDetailsResponse>

    @POST(WITHDRAW_MONEY)
    suspend fun callWithdrawMoneyApi(
        @Header("Authorization") token: String,
        @Body request: WithdrawMoneyRequest
    ): Response<WithdrawMoneyResponse>


    @POST(ROUTE_LIST)
    suspend fun callShuttleRouteApi(
        @Header("Authorization") token: String
    ): Response<ShuttleRouteResponse>

    @POST(SET_RIDE_TYPE)
    suspend fun callSetShuttleRouteApi(
        @Header("Authorization") token: String,
        @Body request: SetShuttleRouteRequest
    ): Response<SetShuttleRouteResponse>

    @POST(MY_VEHICLES)
    suspend fun callMyVehiclesApi(
        @Header("Authorization") token: String
    ): Response<MyVehiclesResponse>

    @POST(VEHICLE_DETAILS)
    suspend fun callVehicleDetailsApi(
        @Header("Authorization") token: String,
        @Body request: VehicleDetailRequest
    ): Response<VehicleDetailResponse>

    @POST(DRIVER_DETAILS)
    suspend fun callDriverDetailsApi(
        @Header("Authorization") token: String,
        @Body request: DriverDetailsRequest
    ): Response<DriverDetailsResponse>



    @POST(COMPUTE_ROUTE)
    suspend fun callComputeRoutesApi(
        @Header("X-Goog-FieldMask") fieldMask: String = " routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline,routes.routeToken",
        @Header("X-Goog-Api-Key") token: String,
        @Body request: ComputeRoutesRequest
    ): Response<ComputeRoutesResponse>

}

