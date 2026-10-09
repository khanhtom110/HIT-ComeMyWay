package com.vetpet.petbeats.data.remote.api.api_springboot

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.CreateClinicRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.CreateClinicResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_admin.response.CreateClinicChildResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiAuthAdmin {
    @POST(ApiConstants.CREATECLINIC)
    suspend fun createClinic(@Body request: CreateClinicRequest): ApiResponse<CreateClinicResponse>

    @GET(ApiConstants.CLINICLIST)
    suspend fun clinicList(
        @Query("activation") activation: String,
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int = 10,
        @Query("cursor") cursor: Int? = null,
    ): ApiResponse<CreateClinicChildResponse>


}