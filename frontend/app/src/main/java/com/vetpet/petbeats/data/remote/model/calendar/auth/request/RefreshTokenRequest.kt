package com.vetpet.petbeats.data.remote.model.calendar.auth.request

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest (
    @SerializedName("refreshToken")
    val refreshTokenRequest: String?
)