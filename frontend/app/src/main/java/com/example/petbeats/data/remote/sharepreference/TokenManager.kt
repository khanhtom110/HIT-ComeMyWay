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


    fun saveUserName(userName: String) {
        prefs.edit().putString("UserName", userName).apply()
    }

    fun getUserName(): String {
        return prefs.getString("UserName", "") ?: ""
    }



    fun clearTokens() {
        prefs.edit().clear().apply()
    }
}