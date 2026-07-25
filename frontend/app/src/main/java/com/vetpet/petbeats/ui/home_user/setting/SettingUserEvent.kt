package com.vetpet.petbeats.ui.home_user.setting

sealed class SettingUserEvent {
    object NavigationEditInformationSetting: SettingUserEvent()
    object NavigationEditPasswordSetting: SettingUserEvent()
    object NavigationLogin: SettingUserEvent()
}