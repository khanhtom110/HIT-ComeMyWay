package com.example.petbeats.ui.home.successAppointment

sealed class SuccessAppointmentEvent {
    object NavigationBooking: SuccessAppointmentEvent()
    object NavigationSearch: SuccessAppointmentEvent()

    data class NavigationConfirm(val id: Int, val clinicId: Int): SuccessAppointmentEvent()
}