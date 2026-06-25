package com.example.petbeats.data.remote.model.calendar.auth.response

data class LoginResponse (
    val accessToken: String,
    val refreshToken: String
)