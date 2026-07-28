package com.vetpet.petbeats.ui.home_user.edit_password_setting

data class EditPasswordSettingState (
    val password: String = "",
    val newPassword: String = "",
    val newPassword1: String = "",

    val isPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isNewPasswordVisible1: Boolean = false,

    val isPassword: Boolean = false,
    val isNewPassword: Boolean = false,
    val isNewPassword1: Boolean = false,

    val passwordError: String = "",
    val passwordNewError: String = "",
)