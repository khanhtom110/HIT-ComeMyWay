package com.vetpet.petbeats.ui.home_user.edit_forgotpassword_setting

data class EditForgotpasswordSettingState (
    val email: String = "",

    val isEmail: Boolean = false,
    val emailError: String = "",
)