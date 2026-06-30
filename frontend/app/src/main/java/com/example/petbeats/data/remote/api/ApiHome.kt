package com.example.petbeats.data.remote.api

import com.example.petbeats.core.network.ApiConstants
import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.data.remote.model.calendar.home.request.ClinicIdRequest
import com.example.petbeats.data.remote.model.calendar.home.request.LocationRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SearchRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SuggestRequest
import com.example.petbeats.data.remote.model.calendar.home.response.ClinicIdResponse
import com.example.petbeats.data.remote.model.calendar.home.response.LocationResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SearchResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SuggestResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiHome {
    @POST(ApiConstants.LOCATION)
    suspend fun location(@Body locationRequest: LocationRequest): ApiResponse<List<LocationResponse>>

    @POST(ApiConstants.CLINICID)
    suspend fun clinicid(@Body clinicIdRequest: ClinicIdRequest): ApiResponse<List<ClinicIdResponse>>

    @POST(ApiConstants.SUGGEST)
    suspend fun suggest(@Body suggestRequest: SuggestRequest): ApiResponse<List<SuggestResponse>>

    @POST(ApiConstants.SEARCH)
    suspend fun search(@Body searchRequest: SearchRequest): ApiResponse<List<SearchResponse>>
}