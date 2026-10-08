package com.vetpet.petbeats.data.remote.api.api_nodejs

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_admin.response.StatisticResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ClinicPostResponseNodeJs
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiAuthAdmin {
    @GET(ApiConstants.CLINICPOSTADMIN)
    suspend fun clinicPostAdmin(
        @Query("status") status: String
    ): ApiResponse<ClinicPostResponseNodeJs>

    @PATCH(ApiConstants.APPROVEADMIN)
    suspend fun approveAdmin(
        @Path("id") id: Int?
    ): ApiResponse<ClinicPostResponseNodeJs>

    @GET(ApiConstants.STATISTICADMIN)
    suspend fun statisticAdmin(): ApiResponse<StatisticResponse>
}