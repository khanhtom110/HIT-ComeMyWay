package com.example.petbeats.ui.auth.login

sealed class LoginEvent {
    object NavigationRegister: LoginEvent()
    object NavigationForgot: LoginEvent()

    //test
    object NavigationHome: LoginEvent()
}