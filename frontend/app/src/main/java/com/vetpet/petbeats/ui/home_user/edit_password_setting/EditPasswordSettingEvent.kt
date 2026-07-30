package com.vetpet.petbeats.ui.home_user.edit_password_setting

sealed class EditPasswordSettingEvent {
    object NavigationForgotPassword: EditPasswordSettingEvent()
    object NavigationSetting: EditPasswordSettingEvent()
}