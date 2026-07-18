package com.example.petbeats.ui.home_user.successAppointment

import com.example.petbeats.ui.home_user.book.adapter.BookChildState

data class SuccessAppointmentState (
    val status: BookChildState = BookChildState.PENDING,
    val address: String = "",
    val date: String = "",
    val time: String = "",
)