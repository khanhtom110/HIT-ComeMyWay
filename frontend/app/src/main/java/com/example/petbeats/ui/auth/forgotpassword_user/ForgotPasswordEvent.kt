package com.example.petbeats.ui.auth.forgotpassword_user

sealed class ForgotPasswordEvent {
    object NavigationLogin: ForgotPasswordEvent()
    data class NavigationOTPSendEmail(val email: String): ForgotPasswordEvent()
}