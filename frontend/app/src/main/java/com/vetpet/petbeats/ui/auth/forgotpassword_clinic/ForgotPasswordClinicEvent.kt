package com.vetpet.petbeats.ui.auth.forgotpassword_clinic

sealed class ForgotPasswordClinicEvent {
    object NavigationForgotPassword: ForgotPasswordClinicEvent()
    object NavigationLogin: ForgotPasswordClinicEvent()
}