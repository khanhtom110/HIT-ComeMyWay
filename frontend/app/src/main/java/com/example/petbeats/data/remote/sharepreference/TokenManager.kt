package com.example.petbeats.data.remote.sharepreference

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("Pet_Prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit().apply {
            putString("ACCESS_TOKEN", accessToken)
            putString("REFRESH_TOKEN", refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString("ACCESS_TOKEN", null)

    fun getRefreshToken(): String? = prefs.getString("REFRESH_TOKEN", null)


    fun saveUserId(userId: Int) {
        prefs.edit().putInt("UserName", userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("UserName", 0)
    }



    fun clearTokens() {
        prefs.edit().clear().apply()
    }
}