package com.vetpet.petbeats.data.remote.dto.auth.request

data class RegisterRequest (
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)