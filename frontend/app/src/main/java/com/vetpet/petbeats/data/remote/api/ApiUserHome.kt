package com.vetpet.petbeats.data.remote.api

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ResetPasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.ForgotPasswordResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.OtpResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AddFriendRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ChangePasswordUserRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.CreateAppointmentRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.CreatePostLocketRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.DeviceTokenFireBaseRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.LinkFriendRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.UpdateProfileRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AddFriendResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AppointmentIdResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.ClinicIdResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.CreateAppointmentResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.CreatePostLocketResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.LinkFriendResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.LocationResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.MyPostLocketResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.SearchResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.TakeAppointmentResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.TakeBookingResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.UpdateProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiUserHome {
    @GET(ApiConstants.LOCATION)
    suspend fun location(
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?
    ): ApiResponse<List<LocationResponse>>

    @GET(ApiConstants.CLINICID)
    suspend fun clinicid(
        @Path("clinicId") clinicId: Int,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?
    ): ApiResponse<ClinicIdResponse>


    @GET(ApiConstants.SEARCH)
    suspend fun search(
        @Query("keyword") keyword: String,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?
    ): ApiResponse<List<SearchResponse>>

    @GET(ApiConstants.TAKEBOOKING)
    suspend fun takeBooking(
        @Path("clinicId") clinicId: Int
    ): ApiResponse<TakeBookingResponse>

    @GET(ApiConstants.CREATAPPOINTMENT)
    suspend fun takeAppointment(): ApiResponse<List<TakeAppointmentResponse>>

    @POST(ApiConstants.CREATAPPOINTMENT)
    suspend fun createAppointment(
        @Body request: CreateAppointmentRequest
    ): ApiResponse<CreateAppointmentResponse>

    @POST(ApiConstants.EDITAPPOINTMENT)
    suspend fun editAppointment(
        @Path("appointmentId") id: Int,
        @Body request: CreateAppointmentRequest
    ): ApiResponse<CreateAppointmentResponse>

    @GET(ApiConstants.TAKEAPPOINTMENTID)
    suspend fun takeAppointmentId(
        @Path("id") id: Int
    ): ApiResponse<AppointmentIdResponse>

    @POST(ApiConstants.CANCELAPPOINTMENT)
    suspend fun cancelAppointment(
        @Path("appointmentId") id: Int
    ): ApiResponse<AppointmentIdResponse>

    @POST(ApiConstants.FIREBASE)
    suspend fun deviceToken(
        @Body request: DeviceTokenFireBaseRequest
    ): ApiResponse<Any>

    @POST(ApiConstants.UPDATEPROFILEUSER)
    suspend fun updateProfile(
        @Path("id") id: Int,
        @Body request: UpdateProfileRequest
    ): ApiResponse<UpdateProfileResponse>

    @GET(ApiConstants.PROFILEUSER)
    suspend fun profile(): ApiResponse<UpdateProfileResponse>

    @POST(ApiConstants.LOGOUT)
    suspend fun logout(
        @Header("Authorization") token: String,
        @Body request: LogoutRequest
    ): ApiResponse<Unit>

    @Multipart
    @POST(ApiConstants.UPLOAD)
    suspend fun uploadImage(
        @Part file: MultipartBody.Part?
    ): ApiResponse<String>

    @POST(ApiConstants.CHANGEPASSWORDUSER)
    suspend fun changePasswordUser(@Body request: ChangePasswordUserRequest): ApiResponse<Any>



    @POST(ApiConstants.RESETPASSWORD)
    suspend fun resetpassword(@Body request: ResetPasswordRequest): ApiResponse<Nothing>



    @POST(ApiConstants.CREATEPOSTLOCKET)
    suspend fun createPostLocket(@Body request: CreatePostLocketRequest): ApiResponse<CreatePostLocketResponse>

    @GET(ApiConstants.GETMYLOCKET)
    suspend fun getMyPostLocket(
        @Query("lastPostId") lastPostId: Int?,
        @Query("size") size: Int = 10
    ): ApiResponse<MyPostLocketResponse>

    @GET(ApiConstants.GETFEEDSLOCKET)
    suspend fun getMyFeedLocket(
        @Query("lastPostId") lastPostId: Int?,
        @Query("size") size: Int = 10
    ): ApiResponse<MyPostLocketResponse>

    @POST(ApiConstants.REJECTFRIENDLOCKET)
    suspend fun rejectFriendLocket(
        @Path("requestId") requestId: Int
    ): ApiResponse<Any>

    @POST(ApiConstants.SENDFRIENDLOCKET)
    suspend fun sendFriendLocket(@Body request: AddFriendRequest): ApiResponse<AddFriendResponse>

    @POST(ApiConstants.ACCEPTFRIENDLOCKET)
    suspend fun acceptFriendLocket(
        @Path("requestId") requestId: Int
    ): ApiResponse<Any>

    @GET(ApiConstants.GETPENDINGFRIENDLOCKET)
    suspend fun getPendingFriendLocket(): ApiResponse<List<AddFriendResponse>>

    @GET(ApiConstants.GETMYFRIENDLOCKET)
    suspend fun getMyFriendLocket(): ApiResponse<List<AddFriendResponse>>

    @GET(ApiConstants.LOCKETLINK)
    suspend fun getLocketLink(): ApiResponse<String>

    @POST(ApiConstants.FINDFRIEND)
    suspend fun findFriend(@Body request: LinkFriendRequest): ApiResponse<LinkFriendResponse>

    @POST(ApiConstants.UNFRIEND)
    suspend fun unFriend(
        @Path("friendId") friendId: Int
    ): ApiResponse<Unit>
}