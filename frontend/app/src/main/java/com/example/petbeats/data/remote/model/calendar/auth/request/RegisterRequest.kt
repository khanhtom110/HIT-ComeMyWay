package com.example.petbeats.data.remote.model.calendar.auth.request

data class RegisterRequest (
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)