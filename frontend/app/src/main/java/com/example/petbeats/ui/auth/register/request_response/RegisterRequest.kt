package com.example.petbeats.ui.auth.register.request_response

data class RegisterRequest (
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)