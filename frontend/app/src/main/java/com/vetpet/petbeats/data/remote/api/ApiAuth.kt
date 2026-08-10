package com.vetpet.petbeats.data.remote.api

import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.core.network.ApiResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.RefreshTokenRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LoginRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.LoginResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.OtpRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.OtpResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.RegisterRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.RegisterResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ResetPasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.ForgotPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuth {
    @POST(ApiConstants.LOGIN)
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST(ApiConstants.REGISTER)
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterResponse>

    @POST(ApiConstants.FORGOTPASSWORD)
    suspend fun forgotpassword(@Body request: ForgotPasswordRequest): ApiResponse<ForgotPasswordResponse>

    @POST(ApiConstants.RESETOTP)
    suspend fun resetotp(@Body request: OtpRequest): ApiResponse<OtpResponse>

    @POST(ApiConstants.REGISTEROTP)
    suspend fun registerotp(@Body request: OtpRequest): ApiResponse<OtpResponse>

    @POST(ApiConstants.RESETPASSWORD)
    suspend fun resetpassword(@Body request: ResetPasswordRequest): ApiResponse<Nothing>

    @POST(ApiConstants.REFRESH)
    fun refresh(@Body request: RefreshTokenRequest): Call<LoginResponse>
}