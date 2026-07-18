package com.example.petbeats.ui.auth.forgotpassword_clinic

import com.example.petbeats.ui.auth.forgotpassword_user.ForgotPasswordEvent

sealed class ForgotPasswordClinicEvent {
    object NavigationForgotPassword: ForgotPasswordClinicEvent()
    object NavigationLogin: ForgotPasswordClinicEvent()
}