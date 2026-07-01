package com.example.petbeats.data.remote.api

import com.example.petbeats.core.network.ApiConstants
import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.data.remote.model.calendar.home.request.SearchRequest
import com.example.petbeats.data.remote.model.calendar.home.request.SuggestRequest
import com.example.petbeats.data.remote.model.calendar.home.response.ClinicIdResponse
import com.example.petbeats.data.remote.model.calendar.home.response.LocationResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SearchResponse
import com.example.petbeats.data.remote.model.calendar.home.response.SuggestResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiHome {
    @GET(ApiConstants.LOCATION)
    suspend fun location(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): ApiResponse<List<LocationResponse>>

    @GET(ApiConstants.CLINICID)
    suspend fun clinicid(
        @Path("clinicId") clinicId: Int
    ): ApiResponse<ClinicIdResponse>

    @GET(ApiConstants.SUGGEST)
    suspend fun suggest(
        @Query("keyword") keyword: String
    ): ApiResponse<List<SuggestResponse>>

    @GET(ApiConstants.SEARCH)
    suspend fun search(
        @Query("keyword") keyword: String,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?
    ): ApiResponse<List<SearchResponse>>
}