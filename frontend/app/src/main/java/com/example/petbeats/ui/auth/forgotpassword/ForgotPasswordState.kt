package com.example.petbeats.ui.auth.forgotpassword

data class ForgotPasswordState (
    val email: String = "",

    val isEmail: Boolean = false,
    val error: String = "",
)