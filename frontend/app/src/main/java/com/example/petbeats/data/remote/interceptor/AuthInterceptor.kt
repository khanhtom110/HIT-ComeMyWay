package com.example.petbeats.data.remote.interceptor

import com.example.petbeats.data.remote.sharepreference.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val accessToken = tokenManager.getAccessToken()

        if (!accessToken.isNullOrEmpty()) {
            request.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(request.build())
    }
}