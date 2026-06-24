package com.example.petbeats.ui.auth.resetpassword.request_response

data class ResetPasswordRequest (
    val token: String,
    val newPassword: String,
    val confirmPassword: String
)