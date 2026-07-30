package com.vetpet.petbeats.ui.home_clinic.appointmentdetail_receive

import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.ServiceItem
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState

data class AppointmentDetailReceiveState (
    val id: Int = 0,
    val imgPet: String = "",
    val status: BookChildState = BookChildState.PENDING,
    val day: String = "",
    val time: String = "",
    val quantity: Int = 0,
    val bookingType: String = "",
    val services: List<ServiceItem> = emptyList(),
    val fullName: String = "",
    val phone: String = "",
    val address: String = "",
    val state: String = ""
)