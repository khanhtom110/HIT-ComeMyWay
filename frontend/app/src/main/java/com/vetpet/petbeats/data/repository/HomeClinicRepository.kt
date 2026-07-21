package com.vetpet.petbeats.data.repository

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ChangePasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ProfileRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ReasonRejectRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ChangePasswordResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ProfileResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AppointmentIdResponse

class HomeClinicRepository(
    private val apiClinicHome: ApiClinicHome
): BaseRepository() {
    suspend fun updateProfile(request: ProfileRequest): DataResult<ProfileResponse> {
        return safeApiCall {
            apiClinicHome.updateProfile(request)
        }
    }

    suspend fun completeProfile(request: ProfileRequest): DataResult<ProfileResponse> {
        return safeApiCall {
            apiClinicHome.completeProfile(request)
        }
    }

    suspend fun changePassword(request: ChangePasswordRequest): DataResult<ChangePasswordResponse> {
        return safeApiCall {
            apiClinicHome.changePassword(request)
        }
    }

    suspend fun rejectAppointment(requestId: AppointmentIdRequest, requestReason: ReasonRejectRequest): DataResult<AppointmentIdResponse> {
        return safeApiCall {
            apiClinicHome.rejectAppointment(requestId, requestReason)
        }
    }

    suspend fun confirmAppointment(request: AppointmentIdRequest): DataResult<AppointmentIdResponse> {
        return safeApiCall {
            apiClinicHome.confirmAppointment(request)
        }
    }

    suspend fun pendingAppointment(request: AppointmentIdRequest): DataResult<List<AppointmentIdResponse>> {
        return safeApiCall {
            apiClinicHome.pendingAppointment()
        }
    }
}