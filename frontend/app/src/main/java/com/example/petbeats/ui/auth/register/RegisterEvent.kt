package com.example.petbeats.ui.auth.register

sealed class RegisterEvent {
    object NavigationRegisterSuccess: RegisterEvent()
    object NavigationLogin: RegisterEvent()
}