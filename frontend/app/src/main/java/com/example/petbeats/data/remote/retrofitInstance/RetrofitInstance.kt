package com.example.petbeats.data.remote.retrofitInstance

import android.content.Context
import com.example.petbeats.core.network.ApiConstants
import com.example.petbeats.data.remote.api.ApiAuth
import com.example.petbeats.data.remote.interceptor.AuthInterceptor
import com.example.petbeats.data.remote.interceptor.TokenAuthenticator
import com.example.petbeats.data.local.sharepreference.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var authenticatedRetrofit: Retrofit? = null

    fun getAuthRetrofit(context: Context): Retrofit {
        if (authenticatedRetrofit == null) {
            val tokenManager = TokenManager(context)

            // Lấy ApiAuth từ "retrofit cũ" để nhét vào Authenticator
            // Không dùng retrofit VIP để refresh, sẽ bị lặp vô hạn
            val apiAuth = retrofit.create(ApiAuth::class.java)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(tokenManager))
                .authenticator(TokenAuthenticator(tokenManager, apiAuth))
                .build()

            // Tạo Retrofit VIP
            authenticatedRetrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return authenticatedRetrofit!!
    }




    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL) //đặt địa chỉ server
            .addConverterFactory(GsonConverterFactory.create()) //Cho Retrofit biết cách xử lý JSON
            .build() //Hoàn thành tạo Retrofit
    }
}