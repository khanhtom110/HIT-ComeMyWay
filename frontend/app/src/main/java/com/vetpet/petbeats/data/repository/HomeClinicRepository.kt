package com.vetpet.petbeats.data.repository

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ChangePasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ProfileRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ReasonRejectRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.AppointmentListClinicResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ChangePasswordResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response.ProfileResponse
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.AppointmentIdResponse
import okhttp3.MultipartBody

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

    suspend fun rejectAppointment(requestId: Int ,requestReason: ReasonRejectRequest): DataResult<AppointmentListClinicResponse> {
        return safeApiCall {
            apiClinicHome.rejectAppointment(requestId, requestReason)
        }
    }

    suspend fun confirmAppointment(request: Int): DataResult<AppointmentListClinicResponse> {
        return safeApiCall {
            apiClinicHome.confirmAppointment(request)
        }
    }

    suspend fun pendingAppointment(): DataResult<List<AppointmentListClinicResponse>> {
        return safeApiCall {
            apiClinicHome.pendingAppointment()
        }
    }

    suspend fun rejectAppointment(): DataResult<List<AppointmentListClinicResponse>> {
        return safeApiCall {
            apiClinicHome.rejectAppointment()
        }
    }

    suspend fun confirmAppointment(): DataResult<List<AppointmentListClinicResponse>> {
        return safeApiCall {
            apiClinicHome.confirmAppointment()
        }
    }

    suspend fun takeAppointmentId(request: AppointmentIdRequest): DataResult<AppointmentListClinicResponse> {
        return safeApiCall {
            apiClinicHome.takeAppointmentId(
                id = request.id
            )
        }
    }

    suspend fun onUploadImage(file: MultipartBody.Part): DataResult<String> {
        return  safeApiCall {
            apiClinicHome.uploadImage(file)
        }
    }

    suspend fun logoutUser(token: String, request: LogoutRequest): DataResult<Unit> {
        return safeApiCall {
            apiClinicHome.logout(token, request)
        }
    }

}