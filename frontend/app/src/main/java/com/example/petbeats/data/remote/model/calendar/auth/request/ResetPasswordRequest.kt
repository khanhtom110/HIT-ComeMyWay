package com.example.petbeats.data.remote.model.calendar.auth.request

data class ResetPasswordRequest (
    val token: String,
    val newPassword: String,
    val confirmPassword: String
)