package com.vetpet.petbeats.ui.auth.resetpassword

sealed class ResetPasswordEvent {
    object NavigationSuccess: ResetPasswordEvent()
    object NavigationLogin: ResetPasswordEvent()

    data class NavigaitonOtpSendEmail(val email: String): ResetPasswordEvent()
}