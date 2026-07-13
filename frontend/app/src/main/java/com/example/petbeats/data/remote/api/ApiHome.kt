package com.example.petbeats.data.remote.api

import com.example.petbeats.core.network.ApiConstants
import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.data.remote.model.calendar.home.request.CreateAppointmentRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SearchRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SuggestRequest
import com.example.petbeats.data.remote.model.calendar.home.response.AppointmentIdResponse
import com.example.petbeats.data.remote.model.calendar.home.response.ClinicIdResponse
import com.example.petbeats.data.remote.model.calendar.home.response.CreateAppointmentResponse
import com.example.petbeats.data.remote.model.calendar.home.response.LocationResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SearchResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SuggestResponse
import com.example.petbeats.data.remote.model.calendar.home.response.TakeAppointmentResponse
import com.example.petbeats.data.remote.model.calendar.home.response.TakeBookingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiHome {
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

}