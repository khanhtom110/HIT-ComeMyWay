package com.example.petbeats.ui.auth.forgotpassword_user

data class ForgotPasswordState (
    val email: String = "",

    val isEmail: Boolean = false,
    val emailError: String = "",
)