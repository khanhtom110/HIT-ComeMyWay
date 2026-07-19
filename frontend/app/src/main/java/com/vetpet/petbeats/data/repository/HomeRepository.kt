package com.vetpet.petbeats.data.repository

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.api.ApiHome
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.ClinicIdRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.CreateAppointmentRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.DeviceTokenFireBaseRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.LocationRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.SearchRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.TakeBookingRequest
import com.vetpet.petbeats.data.remote.dto.calendar.home.response.AppointmentIdResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home.response.ClinicIdResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home.response.CreateAppointmentResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home.response.LocationResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home.response.SearchResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home.response.TakeAppointmentResponse
import com.vetpet.petbeats.data.remote.dto.calendar.home.response.TakeBookingResponse

class HomeRepository(
    private val apiHome: ApiHome
): BaseRepository() {
    suspend fun location(request: LocationRequest): DataResult<List<LocationResponse>> {
        return safeApiCall {
            apiHome.location(
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun clinicid(request: ClinicIdRequest): DataResult<ClinicIdResponse> {
        return safeApiCall {
            apiHome.clinicid(
                clinicId = request.id,
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun search(request: SearchRequest): DataResult<List<SearchResponse>> {
        return safeApiCall {
            apiHome.search(
                keyword = request.keyword,
                latitude = request.latitude,
                longitude = request.longitude
            )
        }
    }

    suspend fun takeBooking(request: TakeBookingRequest): DataResult<TakeBookingResponse> {
        return safeApiCall {
            apiHome.takeBooking(
                clinicId = request.clinicId
            )
        }
    }

    suspend fun takeAppointment(): DataResult<List<TakeAppointmentResponse>> {
        return safeApiCall {
            apiHome.takeAppointment()
        }
    }

    suspend fun createAppointment(request: CreateAppointmentRequest): DataResult<CreateAppointmentResponse> {
        return safeApiCall {
            apiHome.createAppointment(request)
        }
    }

    suspend fun editAppointment(id: AppointmentIdRequest, request: CreateAppointmentRequest): DataResult<CreateAppointmentResponse> {
        return safeApiCall {
            apiHome.editAppointment(
                id = id.id,
                request = request
            )
        }
    }

    suspend fun takeAppointmentId(request: AppointmentIdRequest): DataResult<AppointmentIdResponse> {
        return safeApiCall {
            apiHome.takeAppointmentId(
                id = request.id
            )
        }
    }

    suspend fun cancelAppointment(request: AppointmentIdRequest): DataResult<AppointmentIdResponse> {
        return safeApiCall {
            apiHome.cancelAppointment(
                id = request.id
            )
        }
    }

    suspend fun deviceToken(request: DeviceTokenFireBaseRequest): DataResult<Any> {
        return safeApiCall {
            apiHome.deviceToken(request)
        }
    }

}