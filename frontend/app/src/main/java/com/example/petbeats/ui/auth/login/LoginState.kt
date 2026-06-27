package com.example.petbeats.ui.auth.login

data class LoginState (
    val name: String = "",
    val password: String = "",

    val isPasswordVisible: Boolean = false,

    val isName: Boolean = false,
    val isPassword: Boolean = false,
    val nameError: String = "",
    val passwordError: String = "",
)