package com.vetpet.petbeats.ui.home_clinic.edit_information_clinic

import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.ServiceItem

data class EditInformationClinicState (
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
    val isService: Boolean = false,
    val isClinic: Boolean = false,
    val isHome: Boolean = false,

    val services: List<String> = listOf(
        "Spa", "Làm đẹp", "Tiêm phòng", "Triệt sản", "Phẫu thuật", "Tẩy giun"
    ),
    val selectService: List<String> = emptyList(),

    val isFormVisible: Boolean = false,
    val inputCount: Int = 1,


    //Error
    val nameError: String = "",
    val phoneError: String = "",
    val linkError: String = "",
    val informationError: String = "",
    val timeError: String = "",
    val serviceError: String = "",
)