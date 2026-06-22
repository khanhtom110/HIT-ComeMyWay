package com.example.petbeats.data.repository

import com.example.petbeats.core.network.ApiResponse
import com.example.petbeats.core.network.LogoutRequest
import com.example.petbeats.data.remote.api.ApiAuth
import com.example.petbeats.ui.auth.login.request_response.LoginRequest
import com.example.petbeats.ui.auth.login.request_response.LoginResponse
import com.example.petbeats.ui.auth.register.request_response.RegisterRequest

class AuthRepository(
    private val apiAuth: ApiAuth
) {
    suspend fun loginUser(request: LoginRequest): Boolean {
        return try {
            val response = apiAuth.login(request)

            return true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun registerUser(request: RegisterRequest): Boolean {
        return try {
            val response = apiAuth.register(request)

            return true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logoutUser(request: LogoutRequest): Boolean {
        return try {


            return true
        } catch (e: Exception) {
            false
        }
    }
}