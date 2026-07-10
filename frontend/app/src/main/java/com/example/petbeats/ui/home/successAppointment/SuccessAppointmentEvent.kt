package com.example.petbeats.ui.home.successAppointment

sealed class SuccessAppointmentEvent {
    object NavigationBooking: SuccessAppointmentEvent()
    object NavigationSearch: SuccessAppointmentEvent()
}