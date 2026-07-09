package com.example.petbeats.ui.home.calendar

import com.example.petbeats.data.remote.model.calendar.home.response.ServiceItem

data class CalendarState (
    //information
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val quantity: String = "",
    val other: String = "",
    val state: String = "",
    val tittle: String = "",
    val thumbnailUrl: String = "",
    val isOperating: Boolean = false,
    val rating: Double = 0.0,

    val informationError: String = "",

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


    //service
    val isClinic: Boolean = false,
    val isHome: Boolean = false,
    val services: List<ServiceItem> = emptyList(),

    )