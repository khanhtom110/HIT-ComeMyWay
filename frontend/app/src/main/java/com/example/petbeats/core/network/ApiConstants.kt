package com.example.petbeats.core.network

object ApiConstants {
    const val BASE_URL = "https://hit-comemyway-api.onrender.com/"
    const val LOGIN = "api/v1/auth/login"
    const val REGISTER = "api/v1/auth/register"
    const val LOGOUT = "api/v1/auth/logout"
    const val FORGOTPASSWORD = "/api/v1/auth/forgot-password"
    const val RESETOTP = "/api/v1/auth/verify-otp"
    const val REGISTEROTP = "/api/v1/auth/verify-register"
    const val RESETPASSWORD = "/api/v1/auth/reset-password"
    const val REFRESH = "/api/v1/auth/refresh"
}