package com.example.petbeats.data.remote.api

import com.example.petbeats.core.network.ApiConstants
import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.ui.auth.login.request_response.LoginRequest
import com.example.petbeats.ui.auth.login.request_response.LoginResponse
import com.example.petbeats.ui.auth.register.request_response.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuth {
    @POST(ApiConstants.LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST(ApiConstants.REGISTER)
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<Nothing>>

    @POST(ApiConstants.LOGOUT)
    suspend fun logout(@Body request: LoginRequest): ApiResponse<Nothing>
}