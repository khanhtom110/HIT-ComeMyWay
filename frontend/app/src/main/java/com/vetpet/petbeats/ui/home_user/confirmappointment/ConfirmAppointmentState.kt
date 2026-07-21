package com.vetpet.petbeats.ui.home_user.confirmappointment

import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.ServiceItem
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState

data class ConfirmAppointmentState (
    val name: String = "",
    val clinicAddress: String = "",
    val thumbnailUrl: String = "",
    val rating: Double = 0.0,
    val state: BookChildState = BookChildState.PENDING,
    val fullName: String = "",
    val phoneUser: String = "",
    val phoneClinic: String = "",
    val bookingType: String = "",
    val homeAddress: String = "",
    val petType: String = "",
    val petCondition: String = "",
    val petQuantity: Int = 0,
    val appointmentDate: String = "",
    val appointmentTime: String = "",
    val services: List<ServiceItem> = emptyList(),
    val status: Boolean = false
)