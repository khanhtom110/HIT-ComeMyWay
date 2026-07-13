package com.example.petbeats.data.repository

import com.example.petbeats.core.base.BaseRepository
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.model.calendar.home.request.AppointmentIdRequest
import com.example.petbeats.data.remote.model.calendar.home.request.ClinicIdRequest
import com.example.petbeats.data.remote.model.calendar.home.request.CreateAppointmentRequest
import com.example.petbeats.data.remote.model.calendar.home.request.LocationRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SearchRequest
import com.example.petbeats.data.remote.model.calendar.home.request.TakeBookingRequest
import com.example.petbeats.data.remote.model.calendar.home.response.AppointmentIdResponse
import com.example.petbeats.data.remote.model.calendar.home.response.ClinicIdResponse
import com.example.petbeats.data.remote.model.calendar.home.response.CreateAppointmentResponse
import com.example.petbeats.data.remote.model.calendar.home.response.LocationResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SearchResponse
import com.example.petbeats.data.remote.model.calendar.home.response.TakeAppointmentResponse
import com.example.petbeats.data.remote.model.calendar.home.response.TakeBookingResponse

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
            apiHome.editAppointment(id = id.id, request)
        }
    }

    suspend fun takeAppointmentId(request: AppointmentIdRequest): DataResult<AppointmentIdResponse> {
        return safeApiCall {
            apiHome.takeAppointmentId(
                id = request.id
            )
        }
    }

}