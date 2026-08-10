package com.vetpet.petbeats.data.remote.model.calendar.auth.request

data class OtpRequest (
    val email: String,
    val otp: String
)