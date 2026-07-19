package com.vetpet.petbeats.data.remote.dto.calendar.auth.response

data class LoginResponse (
    val accessToken: String,
    val refreshToken: String,
    val userId: Int,
    val role: String,
    val accountStatus: String
)