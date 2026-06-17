package com.example.petbeats.ui.auth.otp

sealed class OtpEvent {
    object NavigationForgot: OtpEvent()

    data class NavigationResetSendEmail(val email: String): OtpEvent()
}