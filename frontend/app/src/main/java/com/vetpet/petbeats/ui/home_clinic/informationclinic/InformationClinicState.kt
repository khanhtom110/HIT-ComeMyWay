package com.vetpet.petbeats.ui.home_clinic.informationclinic

data class InformationClinicState (
    //information
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val link: String = "",
    val state: String = "",

    val isInformation: Boolean = false,
    val isName: Boolean = false,
    val isPhone: Boolean = false,
    val isAddress: Boolean = false,
    val isLink: Boolean = false,
    val isInputState: Boolean = false,


    //Error
    val nameError: String = "",
    val phoneError: String = "",
    val linkError: String = "",
    val informationError: String = ""
)