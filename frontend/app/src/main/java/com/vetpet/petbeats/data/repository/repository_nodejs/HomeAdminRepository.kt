package com.vetpet.petbeats.data.repository.repository_nodejs

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.api_nodejs.ApiAdminHome
import com.vetpet.petbeats.data.remote.model.calendar.home_admin.response.StatisticResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ClinicPostResponseNodeJs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeAdminRepository @Inject constructor(
    private val apiAdminHome: ApiAdminHome
): BaseRepository()  {
    suspend fun clinicPostAdmin(status: String): DataResult<ClinicPostResponseNodeJs> {
        return safeApiCall {
            apiAdminHome.clinicPostAdmin(status)
        }
    }

    suspend fun approveAdmin(id: Int): DataResult<ClinicPostResponseNodeJs> {
        return safeApiCall {
            apiAdminHome.approveAdmin(id)
        }
    }

    suspend fun statisticAdmin(): DataResult<StatisticResponse> {
        return safeApiCall {
            apiAdminHome.statisticAdmin()
        }
    }
}