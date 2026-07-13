package com.example.petbeats.ui.home.confirmappointment

sealed class ConfirmAppointmentEvent {
    object NavigationHomeAppointment: ConfirmAppointmentEvent()
    object NavigationSearch: ConfirmAppointmentEvent()

    data class NavigationEditAppointment(val id: Int, val clinicId: Int): ConfirmAppointmentEvent()
}