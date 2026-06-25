package com.example.petbeats.data.remote.api

import com.example.petbeats.core.network.ApiConstants
import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.example.petbeats.data.remote.model.calendar.auth.request.RefreshTokenRequest
import com.example.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import com.example.petbeats.data.remote.model.calendar.auth.request.LoginRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.LoginResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.OtpRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.OtpResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.RegisterRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.RegisterResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.ResetPasswordRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuth {
    @POST(ApiConstants.LOGIN)
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST(ApiConstants.REGISTER)
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterResponse>

    @POST(ApiConstants.LOGOUT)
    suspend fun logout(@Body request: LogoutRequest): ApiResponse<Nothing>

    @POST(ApiConstants.FORGOTPASSWORD)
    suspend fun forgotpassword(@Body request: ForgotPasswordRequest): ApiResponse<Nothing>

    @POST(ApiConstants.RESETOTP)
    suspend fun resetotp(@Body request: OtpRequest): ApiResponse<OtpResponse>

    @POST(ApiConstants.REGISTEROTP)
    suspend fun registerotp(@Body request: OtpRequest): ApiResponse<OtpResponse>

    @POST(ApiConstants.RESETPASSWORD)
    suspend fun resetpassword(@Body request: ResetPasswordRequest): ApiResponse<Nothing>

    @POST(ApiConstants.REFRESH)
    fun refresh(@Body request: RefreshTokenRequest): Call<LoginResponse>
}