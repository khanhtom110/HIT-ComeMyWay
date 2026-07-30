package com.vetpet.petbeats.ui.home_user.edit_resetpassword_setting

sealed class EditResetpasswordSettingEvent {
    object NavigationEditpasswordSuccess: EditResetpasswordSettingEvent()
    object NavigationSetting: EditResetpasswordSettingEvent()

    data class NavigaitonOtpSendEmail(val email: String): EditResetpasswordSettingEvent()
}