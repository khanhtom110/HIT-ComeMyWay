package com.vetpet.petbeats.data.repository.repository_springboot

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.api_springboot.ApiAuthAdmin
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.CreateClinicRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.CreateClinicResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_admin.response.CreateClinicChildResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthAdminRepository @Inject constructor(
    private val apiAuthAdmin: ApiAuthAdmin
): BaseRepository() {

    suspend fun createClinic(request: CreateClinicRequest): DataResult<CreateClinicResponse> {
        return safeApiCall {
            apiAuthAdmin.createClinic(request)
        }
    }

    suspend fun clinicList(activation: String, keyword: String, limit: Int, cursor: Int?): DataResult<CreateClinicChildResponse> {
        return safeApiCall {
            apiAuthAdmin.clinicList(activation, keyword, limit, cursor)
        }
    }
}