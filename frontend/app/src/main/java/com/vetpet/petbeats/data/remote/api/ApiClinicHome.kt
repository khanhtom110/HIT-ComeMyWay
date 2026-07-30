package com.vetpet.petbeats.data.remote.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ChangePasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ProfileRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ReasonRejectRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.AppointmentListClinicResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ChangePasswordResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ProfileResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AppointmentIdResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiClinicHome {
    @POST(ApiConstants.UPDATEPROFILE)
    suspend fun updateProfile(
        @Body request: ProfileRequest
    ): ApiResponse<ProfileResponse>

    @POST(ApiConstants.COMPLETEPROFILE)
    suspend fun completeProfile(
        @Body request: ProfileRequest
    ): ApiResponse<ProfileResponse>

    @POST(ApiConstants.CHANGEPASSWORD)
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): ApiResponse<ChangePasswordResponse>

    @POST(ApiConstants.REJECTAPPOINTMENTPOST)
    suspend fun rejectAppointment(
        @Path("appointmentId") appointmentId: Int,
        @Body requestReason: ReasonRejectRequest
    ): ApiResponse<AppointmentListClinicResponse>

    @POST(ApiConstants.CONFIRMAPPOINTMENTPOST)
    suspend fun confirmAppointment(
        @Path("appointmentId") appointmentId: Int
    ): ApiResponse<AppointmentListClinicResponse>

    @GET(ApiConstants.PENDINGAPPOINTMENTGET)
    suspend fun pendingAppointment(): ApiResponse<List<AppointmentListClinicResponse>>

    @GET(ApiConstants.REJECTAPPOINTMENTGET)
    suspend fun rejectAppointment(): ApiResponse<List<AppointmentListClinicResponse>>

    @GET(ApiConstants.CONFIRMAPPOINTMENTGET)
    suspend fun confirmAppointment(): ApiResponse<List<AppointmentListClinicResponse>>

    @GET(ApiConstants.TAKEAPPOINTMENTIDCLINIC)
    suspend fun takeAppointmentId(
        @Path("id") id: Int
    ): ApiResponse<AppointmentListClinicResponse>

    @Multipart
    @POST(ApiConstants.UPLOAD)
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): ApiResponse<String>

    @POST(ApiConstants.LOGOUT)
    suspend fun logout(
        @Header("Authorization") token: String,
        @Body request: LogoutRequest
    ): ApiResponse<Unit>

}