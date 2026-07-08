package com.example.petbeats.ui.home.calendar

data class CalendarState (
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val quantity: String = "",
    val other: String = "",

    val informationError: String = "",

    val isName: Boolean = false,
    val isPhone: Boolean = false,
    val isAddress: Boolean = false,
    val isQuantity: Boolean = false,

    val isInformation: Boolean = false,

    val isDog: Boolean = false,
    val isCat: Boolean = false,
    val isOther: Boolean = false,
    val isInputOther: Boolean = false

)