package com.vetpet.petbeats.data.remote.api

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home_clinic.request.ChangePasswordRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home_clinic.request.ProfileRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home_clinic.request.ReasonRejectRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home_clinic.response.ChangePasswordResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home_clinic.response.ProfileResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home_user.response.AppointmentIdResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST(ApiConstants.REJECTAPPOINTMENT)
    suspend fun rejectAppointment(
        @Body requestId: AppointmentIdRequest,
        @Body requestReason: ReasonRejectRequest
    ): ApiResponse<AppointmentIdResponse>

    @POST(ApiConstants.CONFIRMAPPOINTMENT)
    suspend fun confirmAppointment(
        @Body requestId: AppointmentIdRequest,
    ): ApiResponse<AppointmentIdResponse>

    @GET(ApiConstants.PENDINGAPPOINTMENT)
    suspend fun pendingAppointment(): ApiResponse<List<AppointmentIdResponse>>
}