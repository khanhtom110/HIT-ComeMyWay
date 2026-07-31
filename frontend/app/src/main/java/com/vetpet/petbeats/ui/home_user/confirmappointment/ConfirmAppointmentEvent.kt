package com.vetpet.petbeats.ui.home_user.confirmappointment

sealed class ConfirmAppointmentEvent {
    object NavigationHomeAppointment: ConfirmAppointmentEvent()
    object NavigationSearch: ConfirmAppointmentEvent()

    data class NavigationEditAppointment(val id: Int, val clinicId: Int): ConfirmAppointmentEvent()
}