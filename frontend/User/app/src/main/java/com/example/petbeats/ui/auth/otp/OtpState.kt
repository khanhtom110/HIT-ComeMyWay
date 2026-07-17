package com.example.petbeats.ui.auth.otp

data class OtpState (
    val otp1: String = "",
    val otp2: String = "",
    val otp3: String = "",
    val otp4: String = "",
    val otp5: String = "",
    val otp6: String = "",

    val isOtp: Boolean = false,
    val otpError: String = "",
)