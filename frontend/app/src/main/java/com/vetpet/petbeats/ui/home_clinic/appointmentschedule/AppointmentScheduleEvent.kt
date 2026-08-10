package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

sealed class AppointmentScheduleEvent {
    object NavigationScheduleList: AppointmentScheduleEvent()

    data class NavigationDetail(val id: Int): AppointmentScheduleEvent()
    data class NavigationDetailReceive(val id: Int): AppointmentScheduleEvent()
    data class NavigationDetailRefuse(val id: Int): AppointmentScheduleEvent()
}