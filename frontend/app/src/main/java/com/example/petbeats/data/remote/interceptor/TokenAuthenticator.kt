package com.example.petbeats.data.remote.interceptor

import com.example.petbeats.data.remote.model.calendar.auth.request.RefreshTokenRequest
import com.example.petbeats.data.local.sharepreference.TokenManager
import com.example.petbeats.data.remote.api.ApiAuth
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenManager: TokenManager,
    private val apiAuth: ApiAuth
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenManager.getRefreshToken()

        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        synchronized(this) {
            // Chạy API đổi token (dùng execute() để chạy đồng bộ)
            val request = RefreshTokenRequest(refreshToken)
            val refreshResponse = apiAuth.refresh(request).execute()

            if (refreshResponse.isSuccessful) {
                val newToken = refreshResponse.body()

                if (newToken != null) {
                    // 1. Cứu thành công! Lưu ngay cặp token mới vào máy
                    tokenManager.saveTokens(newToken.accessToken, newToken.refreshToken)

                    // 2. Nhét cái vé Access Token MỚI vào lại cái API vừa bị tạch
                    return response.request()?.newBuilder()
                        ?.header("Authorization", "Bearer ${newToken.accessToken}")
                        ?.build() // Trả về request mới, hệ thống sẽ tự động gọi lại API đó!
                }
                else {
                    tokenManager.clearTokens()
                    return null
                }
            }
        }
        return null
    }
}