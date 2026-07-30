package com.vetpet.petbeats.ui.auth.login

sealed class LoginEvent {
    object NavigationRegister: LoginEvent()
    object NavigationForgot: LoginEvent()


    //test
    data class NavigationUserHome(val accessToken: String, val refreshToken: String, val userId: Int): LoginEvent()
    data class NavigationClinicHome(val accessToken: String, val refreshToken: String, val userId: Int): LoginEvent()
    data class NavigationChangePassword(val accessToken: String, val refreshToken: String, val userId: Int): LoginEvent()
    data class NavigationLoginSuccess(val accessToken: String, val refreshToken: String, val userId: Int): LoginEvent()

}