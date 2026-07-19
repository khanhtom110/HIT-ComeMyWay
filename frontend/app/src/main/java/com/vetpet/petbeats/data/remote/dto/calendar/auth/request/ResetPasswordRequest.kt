package com.vetpet.petbeats.data.remote.dto.calendar.auth.request

data class ResetPasswordRequest (
    val token: String,
    val newPassword: String,
    val confirmPassword: String
)