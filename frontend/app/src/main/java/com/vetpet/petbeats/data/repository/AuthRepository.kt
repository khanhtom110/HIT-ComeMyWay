package com.vetpet.petbeats.data.repository

import com.vetpet.petbeats.core.base.BaseRepository
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.api.ApiAuth
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LoginRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.LoginResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.OtpRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.OtpResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.RegisterRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.RegisterResponse
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ResetPasswordRequest
import com.vetpet.petbeats.data.remote.model.calendar.auth.response.ForgotPasswordResponse

class AuthRepository(
    private val apiAuth: ApiAuth
): BaseRepository() {

    suspend fun loginUser(request: LoginRequest): DataResult<LoginResponse> {
        return safeApiCall {
            apiAuth.login(request)
        }
    }

    suspend fun registerUser(request: RegisterRequest): DataResult<RegisterResponse> {
        return safeApiCall {
            apiAuth.register(request)
        }
    }

    suspend fun forgotPasswordUser(request: ForgotPasswordRequest): DataResult<ForgotPasswordResponse> {
        return safeApiCall {
            apiAuth.forgotpassword(request)
        }
    }

    suspend fun resetOtpUser(request: OtpRequest): DataResult<OtpResponse> {
        return safeApiCall {
            apiAuth.resetotp(request)
        }
    }

    suspend fun registerOtpUser(request: OtpRequest): DataResult<OtpResponse> {
        return safeApiCall {
            apiAuth.registerotp(request)
        }
    }

    suspend fun resetpasswordUser(request: ResetPasswordRequest): DataResult<Nothing> {
        return safeApiCall {
            apiAuth.resetpassword(request)
        }
    }
}