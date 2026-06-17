package com.example.petbeats.ui.auth.resetpassword

sealed class ResetPasswordEvent {
    object NavigationOTP: ResetPasswordEvent()
    object NavigationSuccess: ResetPasswordEvent()
}