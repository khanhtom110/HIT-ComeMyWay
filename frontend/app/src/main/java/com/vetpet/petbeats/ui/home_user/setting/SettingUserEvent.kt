package com.vetpet.petbeats.ui.home_user.setting

sealed class SettingUserEvent {
    object NavigationEditPasswordSetting: SettingUserEvent()
    object NavigationLogin: SettingUserEvent()


    data class NavigationEditInformationSetting(val id: Int): SettingUserEvent()
}