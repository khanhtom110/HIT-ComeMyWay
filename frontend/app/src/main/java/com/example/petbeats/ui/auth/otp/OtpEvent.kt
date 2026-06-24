package com.example.petbeats.ui.auth.otp

sealed class OtpEvent {
    object NavigationVector: OtpEvent()

    data class NavigationSendToken(val token: String): OtpEvent()
}