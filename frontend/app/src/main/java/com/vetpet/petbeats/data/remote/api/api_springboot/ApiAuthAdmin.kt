package com.vetpet.petbeats.data.remote.api.api_springboot

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.CreateClinicRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.CreateClinicResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuthAdmin {
    @POST(ApiConstants.CREATECLINIC)
    suspend fun createClinic(@Body request: CreateClinicRequest): ApiResponse<CreateClinicResponse>
}