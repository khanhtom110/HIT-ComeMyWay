package com.example.petbeats.ui.home.confirmappointment

sealed class ConfirmAppointmentEvent {
    object NavigationSuccessAppointment: ConfirmAppointmentEvent()
    data class NavigationCalendar(val id: Int): ConfirmAppointmentEvent()
}