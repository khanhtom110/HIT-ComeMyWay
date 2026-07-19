package com.vetpet.petbeats.data.remote.dto.calendar.auth.request

data class OtpRequest (
    val email: String,
    val otp: String
)