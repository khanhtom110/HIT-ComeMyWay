package com.example.petbeats.data.repository

import com.example.petbeats.core.base.BaseRepository
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.data.local.dao.HistoryDao
import com.example.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.example.petbeats.data.remote.api.ApiAuth
import com.example.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import com.example.petbeats.data.remote.model.calendar.auth.request.LoginRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.LoginResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.OtpRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.OtpResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.RegisterRequest
import com.example.petbeats.data.remote.model.calendar.auth.response.RegisterResponse
import com.example.petbeats.data.remote.model.calendar.auth.request.ResetPasswordRequest
import com.example.petbeats.data.utils.ErrorUtils.getErrorTargetAndMessage
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException

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

    suspend fun forgotpasswordUser(request: ForgotPasswordRequest): DataResult<RegisterResponse> {
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

    suspend fun logoutUser(request: LogoutRequest): DataResult<Nothing> {
        return safeApiCall {
            apiAuth.logout(request)
        }
    }
}