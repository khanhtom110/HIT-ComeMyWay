package com.example.petbeats.ui.home.confirmappointment

import com.example.petbeats.data.remote.model.calendar.home.response.ServiceItem

data class ConfirmAppointmentState (
    val name: String = "",
    val clinicAddress: String = "",
    val thumbnailUrl: String = "",
    val rating: Double = 0.0,
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