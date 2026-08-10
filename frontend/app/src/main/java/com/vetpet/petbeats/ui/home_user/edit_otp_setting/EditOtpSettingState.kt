package com.vetpet.petbeats.ui.home_user.edit_otp_setting

data class EditOtpSettingState (
    val otp1: String = "",
    val otp2: String = "",
    val otp3: String = "",
    val otp4: String = "",
    val otp5: String = "",
    val otp6: String = "",

    val isOtp: Boolean = false,
    val otpError: String = "",
)