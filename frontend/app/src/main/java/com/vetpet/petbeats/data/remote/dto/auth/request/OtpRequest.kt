package com.vetpet.petbeats.data.remote.dto.auth.request

data class OtpRequest (
    val email: String,
    val otp: String
)