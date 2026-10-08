package com.vetpet.petbeats.data.remote.retrofitInstance

import android.content.Context
import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.data.remote.api.api_nodejs.ApiAuthAdmin
import com.vetpet.petbeats.data.remote.api.api_nodejs.ApiClinicPost
import com.vetpet.petbeats.data.remote.api.api_springboot.ApiAuth
import com.vetpet.petbeats.data.remote.api.api_springboot.ApiClinicHome
import com.vetpet.petbeats.data.remote.api.api_springboot.ApiUserHome
import com.vetpet.petbeats.data.remote.interceptor.AuthInterceptor
import com.vetpet.petbeats.data.remote.interceptor.TokenAuthenticator
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstanceNodeJS {
    //Authenticated Retrofit dùng cho các API cần đăng nhập
    @Provides
    @Singleton
    @Named("NodeAuthRetrofit")
    fun provideAuthRetrofit(
        @ApplicationContext context: Context,
        tokenManager: TokenManager,
        apiAuth: ApiAuth
    ): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .authenticator(TokenAuthenticator(context, tokenManager, apiAuth))
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.NODE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //User dùng cho các API bên admin
    @Provides
    @Singleton
    fun provideApiAdminHome(@Named("NodeAuthRetrofit") retrofit: Retrofit): ApiAuthAdmin {
        return retrofit.create(ApiAuthAdmin::class.java)
    }

    //Clinic dùng cho các API bên clinic
    @Provides
    @Singleton
    fun provideApiClinicHome(@Named("NodeAuthRetrofit") retrofit: Retrofit): ApiClinicPost {
        return retrofit.create(ApiClinicPost::class.java)
    }
}