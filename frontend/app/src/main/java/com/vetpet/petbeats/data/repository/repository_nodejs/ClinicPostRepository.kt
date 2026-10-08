package com.vetpet.petbeats.data.repository.repository_nodejs

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.api_nodejs.ApiClinicPost
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ClinicPostRequestNodeJs
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ClinicDeleteResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ClinicPostResponseNodeJs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClinicPostRepository @Inject constructor(
    private val apiClinicPost: ApiClinicPost
): BaseRepository() {
    suspend fun clinicPostIdNodeJS(id: Int): DataResult<ClinicPostResponseNodeJs> {
        return safeApiCall {
            apiClinicPost.clinicPostIdNodeJS(id)
        }
    }

    suspend fun clinicPostNodeJS(request: ClinicPostRequestNodeJs): DataResult<ClinicPostResponseNodeJs> {
        return safeApiCall {
            apiClinicPost.clinicPostNodeJS(request)
        }
    }

    suspend fun clinicGetNodeJS(): DataResult<List<ClinicPostResponseNodeJs>> {
        return  safeApiCall {
            apiClinicPost.clinicGetNodeJS()
        }
    }

    suspend fun clinicDelete(id: Int): DataResult<ClinicDeleteResponse> {
        return safeApiCall {
            apiClinicPost.clinicDelete(id)
        }
    }

    suspend fun clinicPostGlobal(): DataResult<List<ClinicPostResponseNodeJs>> {
        return safeApiCall {
            apiClinicPost.clinicPostGlobal()
        }
    }
}