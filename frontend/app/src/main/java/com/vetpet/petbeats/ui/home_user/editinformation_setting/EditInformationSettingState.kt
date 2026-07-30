package com.vetpet.petbeats.ui.home_user.editinformation_setting

data class EditInformationSettingState (
    val id: Int = 0,
    val image: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = "",


    val isImage: Boolean = false,
    val isName: Boolean = false,
    val isPhone: Boolean = false,
    val isAddress: Boolean = false,


    val nameError: String = "",
    val phoneError: String = "",
)