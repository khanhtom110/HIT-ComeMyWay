package com.vetpet.petbeats.ui.home_clinic.informationclinic

import com.vetpet.petbeats.data.remote.dto.calendar.home_user.response.ServiceItem

data class InformationClinicState (
    //image
    val image: String = "",

    val isImage: Boolean = false,

    //information
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val link: String = "",
    val openHour: String = "",
    val openMinute: String = "",
    val closeHour: String = "",
    val closeMinute: String = "",
    val state: String = "",

    val isInformation: Boolean = false,
    val isName: Boolean = false,
    val isPhone: Boolean = false,
    val isAddress: Boolean = false,
    val isLink: Boolean = false,
    val isTime: Boolean = false,
    val isInputState: Boolean = false,

    //Service
    val bookingType: String = "",

    val isService: Boolean = false,
    val isClinic: Boolean = false,
    val isHome: Boolean = false,
    val services: List<String> = emptyList(),
    val selectService: List<Int> = emptyList(),


    //Error
    val nameError: String = "",
    val phoneError: String = "",
    val linkError: String = "",
    val informationError: String = "",
    val timeError: String = "",
    val serviceError: String = "",
)