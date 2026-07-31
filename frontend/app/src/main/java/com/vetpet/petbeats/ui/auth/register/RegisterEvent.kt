package com.vetpet.petbeats.ui.auth.register

sealed class RegisterEvent {
    object NavigationLogin: RegisterEvent()

    data class NavigationRegisterSendEmail(val email: String):  RegisterEvent()
}