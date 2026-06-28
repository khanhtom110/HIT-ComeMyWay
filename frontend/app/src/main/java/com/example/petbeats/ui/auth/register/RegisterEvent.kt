package com.example.petbeats.ui.auth.register

import com.example.petbeats.ui.auth.forgotpassword.ForgotPasswordEvent

sealed class RegisterEvent {
    object NavigationLogin: RegisterEvent()

    data class NavigationRegisterSendEmail(val email: String):  RegisterEvent()
}