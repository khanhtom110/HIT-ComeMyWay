package com.vetpet.petbeats.ui.home_user.edit_otp_setting

sealed class EditOtpSettingEvent {
    object NavigationForgotpasswordSetting: EditOtpSettingEvent()

    data class NavigationSendToken(val token: String, val email: String): EditOtpSettingEvent()
}