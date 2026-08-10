package com.vetpet.petbeats.ui.auth.changepassword_clinic

data class ChangePasswordState (
    val password: String = "",
    val password1: String = "",

    val isPasswordVisible: Boolean = false,
    val isPasswordVisible1: Boolean = false,

    val isPassword: Boolean = false,
    val isPassword1: Boolean = false,
    val passwordError: String = "",
    val passwordError1: String = ""
)