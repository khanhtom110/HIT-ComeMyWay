package com.vetpet.petbeats.ui.home_user.edit_forgotpassword_setting

sealed class EditForgotpasswordSettingEvent {
    object NavigationSetting: EditForgotpasswordSettingEvent()
    data class NavigationOTPSendEmail(val email: String): EditForgotpasswordSettingEvent()
}