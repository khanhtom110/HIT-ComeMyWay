package com.example.petbeats.ui.auth.login.request_response

data class LoginResponse (
    val accessToken: String,
    val refreshToken: String
)