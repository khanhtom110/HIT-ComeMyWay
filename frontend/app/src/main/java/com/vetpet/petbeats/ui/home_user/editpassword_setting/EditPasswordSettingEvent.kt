package com.vetpet.petbeats.ui.home_user.editpassword_setting

sealed class EditPasswordSettingEvent {
    object NavigationForgotPassword: EditPasswordSettingEvent()
    object NavigationSetting: EditPasswordSettingEvent()
}