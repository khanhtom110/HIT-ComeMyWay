package com.example.petbeats.ui.auth.login

sealed class LoginEvent {
    object NavigationRegister: LoginEvent()
    object NavigationForgot: LoginEvent()

    //test
    data class NavigationHome(val accessToken: String, val refreshToken: String, val userId: Int): LoginEvent()
    data class NavigationClinic(val accessToken: String, val refreshToken: String): LoginEvent()
}