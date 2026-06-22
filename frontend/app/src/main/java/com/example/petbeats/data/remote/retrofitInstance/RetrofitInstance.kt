package com.example.petbeats.data.remote.retrofitInstance

import com.example.petbeats.core.network.ApiConstants
import com.example.petbeats.data.remote.api.ApiAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL) //đặt địa chỉ server
            .addConverterFactory(GsonConverterFactory.create()) //Cho Retrofit biết cách xử lý JSON
            .build() //Hoàn thành tạo Retrofit
    }
}