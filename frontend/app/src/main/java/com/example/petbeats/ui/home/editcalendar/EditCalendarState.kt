package com.example.petbeats.ui.home.editcalendar

import com.example.petbeats.data.remote.model.calendar.home.response.ServiceItem

data class EditCalendarState (
    //information
    val id: Int = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val quantity: Int = 0,
    val other: String = "",
    val state: String = "",
    val tittle: String = "",
    val thumbnailUrl: String = "",
    val isOperating: Boolean = false,
    val rating: Double = 0.0,

    val isName: Boolean = false,
    val isPhone: Boolean = false,
    val isAddress: Boolean = false,
    val isQuantity: Boolean = false,

    val isInformation: Boolean = false,

    val isDog: Boolean = false,
    val isCat: Boolean = false,
    val isOther: Boolean = false,
    val isInputOther: Boolean = false,
    val isInputState: Boolean = false,
    val petType: String = "",


    //service
    val isService: Boolean = false,
    val isClinic: Boolean = false,
    val isHome: Boolean = false,
    val bookingType: String = "",
    val services: List<ServiceItem> = emptyList(),

    val selectService: List<Int> = emptyList(),

    //calendar
    val isCalendar: Boolean = false,
    val appointmentDate: String = "",

    //time
    val isTime: Boolean = false,
    val hour: String = "",
    val minute: String = "",
    val appointmentTime: String = "",

    //Error
    val phoneError: String = "",
    val informationError: String = "",
    val serviceError: String = "",
    val calendarError: String = "",
    val timeError: String = "",
    val quantityError: String = "",
    val nameError: String = ""
)