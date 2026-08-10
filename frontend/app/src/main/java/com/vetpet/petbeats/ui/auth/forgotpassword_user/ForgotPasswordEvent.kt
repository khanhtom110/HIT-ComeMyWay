package com.vetpet.petbeats.ui.auth.forgotpassword_user

sealed class ForgotPasswordEvent {
    object NavigationLogin: ForgotPasswordEvent()
    object NavigationForgotClinicSuccess: ForgotPasswordEvent()
    data class NavigationOTPSendEmail(val email: String): ForgotPasswordEvent()
}