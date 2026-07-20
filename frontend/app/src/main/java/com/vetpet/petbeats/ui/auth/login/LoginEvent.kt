package com.vetpet.petbeats.ui.auth.login

sealed class LoginEvent {
    object NavigationRegister: LoginEvent()
    object NavigationForgot: LoginEvent()
    object NavigationLoginSuccess: LoginEvent()

    //test
    data class NavigationHome(val accessToken: String, val refreshToken: String, val userId: Int): LoginEvent()
    data class NavigationChangePassword(val accessToken: String, val refreshToken: String, val userId: Int): LoginEvent()
}