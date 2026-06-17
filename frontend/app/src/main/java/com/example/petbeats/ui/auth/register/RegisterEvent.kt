package com.example.petbeats.ui.auth.register

sealed class RegisterEvent {
    object NavigationLogin: RegisterEvent()
}