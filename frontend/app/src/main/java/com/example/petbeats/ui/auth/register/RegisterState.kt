package com.example.petbeats.ui.auth.register

data class RegisterState (
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val password1: String = "",

    val isPasswordVisible: Boolean = false,
    val isPasswordVisible1: Boolean = false,

    val isName: Boolean = false,
    val isEmail: Boolean = false,
    val isPassword: Boolean = false,
    val isPassword1: Boolean = false,
    val nameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val passwordError1: String = ""
)