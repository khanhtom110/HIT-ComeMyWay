package com.example.petbeats.data.repository

import android.util.Log
import com.example.petbeats.core.base.BaseRepository
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.remote.model.calendar.home.request.ClinicIdRequest
import com.example.petbeats.data.remote.model.calendar.home.request.LocationRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SearchRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SuggestRequest
import com.example.petbeats.data.remote.model.calendar.home.response.ClinicIdResponse
import com.example.petbeats.data.remote.model.calendar.home.response.LocationResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SearchResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SuggestResponse

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
                clinicId = request.id
            )
        }
    }

    suspend fun suggest(request: SuggestRequest): DataResult<List<SuggestResponse>> {
        return safeApiCall {
            apiHome.suggest(
                keyword = request.keyword
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
}