package com.example.petbeats.ui.home.successAppointment

import com.example.petbeats.ui.home.book.adapter.BookChildState

data class SuccessAppointmentState (
    val status: BookChildState = BookChildState.PENDING,
    val address: String = "",
    val date: String = "",
    val time: String = "",
)