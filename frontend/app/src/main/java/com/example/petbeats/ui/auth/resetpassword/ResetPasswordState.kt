package com.example.petbeats.ui.auth.resetpassword

data class ResetPasswordState (
    val password: String = "",
    val password1: String = "",

    val isPasswordVisible: Boolean = false,
    val isPasswordVisible1: Boolean = false,

    val isPassword: Boolean = false,
    val isPassword1: Boolean = false,
    val passwordError: String = "",
    val passwordError1: String = ""
)