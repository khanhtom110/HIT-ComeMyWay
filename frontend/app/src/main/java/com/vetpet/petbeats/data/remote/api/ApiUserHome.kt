package com.vetpet.petbeats.data.remote.api

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.CreateAppointmentRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.DeviceTokenFireBaseRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AppointmentIdResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.ClinicIdResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.CreateAppointmentResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.LocationResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.SearchResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.TakeAppointmentResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.TakeBookingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

}