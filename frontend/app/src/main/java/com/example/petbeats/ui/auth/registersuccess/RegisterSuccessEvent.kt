package com.example.petbeats.ui.auth.registersuccess

sealed class RegisterSuccessEvent {
    object NavigationLogin: RegisterSuccessEvent()
}