package com.vetpet.petbeats.data.remote.retrofitInstance

import android.content.Context
import com.vetpet.petbeats.core.network.ApiConstants
import com.vetpet.petbeats.data.remote.api.ApiAuth
import com.vetpet.petbeats.data.remote.api.ApiClinicHome
import com.vetpet.petbeats.data.remote.api.ApiUserHome
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
object RetrofitInstance {

    // Provide TokenManager để Hilt tự quản lý
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    // Base Retrofit dùng để Login, Register hoặc Refresh Token
    @Provides
    @Singleton
    @Named("BaseRetrofit")
    fun provideBaseRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provide ApiAuth dùng để sử dụng Base Retrofit
    @Provides
    @Singleton
    fun provideApiAuth(@Named("BaseRetrofit") retrofit: Retrofit): ApiAuth {
        return retrofit.create(ApiAuth::class.java)
    }

    //Authenticated Retrofit dùng cho các API cần đăng nhập
    @Provides
    @Singleton
    @Named("AuthRetrofit")
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
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //User dùng cho các API bên user
    @Provides
    @Singleton
    fun provideApiUserHome(@Named("AuthRetrofit") retrofit: Retrofit): ApiUserHome {
        return retrofit.create(ApiUserHome::class.java)
    }

    //Clinic dùng cho các API bên clinic
    @Provides
    @Singleton
    fun provideApiClinicHome(@Named("AuthRetrofit") retrofit: Retrofit): ApiClinicHome {
        return retrofit.create(ApiClinicHome::class.java)
    }

}