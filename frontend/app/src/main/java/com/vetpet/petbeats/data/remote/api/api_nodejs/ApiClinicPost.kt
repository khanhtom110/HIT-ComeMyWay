package com.vetpet.petbeats.data.remote.api.api_nodejs

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ClinicPostRequestNodeJs
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ClinicDeleteResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ClinicPostResponseNodeJs
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiClinicPost {
    @GET(ApiConstants.CLINICPOSTIDNODEJS)
    suspend fun clinicPostIdNodeJS(
        @Path("id") id: Int
    ): ApiResponse<ClinicPostResponseNodeJs>

    @POST(ApiConstants.CLINICPOSTNODEJS)
    suspend fun clinicPostNodeJS(@Body request: ClinicPostRequestNodeJs): ApiResponse<ClinicPostResponseNodeJs>

    @GET(ApiConstants.CLINICPOSTNODEJS)
    suspend fun clinicGetNodeJS(): ApiResponse<List<ClinicPostResponseNodeJs>>

    @DELETE(ApiConstants.CLINICDELETE)
    suspend fun clinicDelete(
        @Path("id") id: Int
    ): ApiResponse<ClinicDeleteResponse>

    @GET(ApiConstants.CLINICPOSTGLOBAL)
    suspend fun clinicPostGlobal(): ApiResponse<List<ClinicPostResponseNodeJs>>
}