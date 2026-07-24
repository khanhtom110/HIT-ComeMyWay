package com.vetpet.petbeats.ui.home_clinic.schedulelist

import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.AppointmentScheduleEvent

sealed class ScheduleListEvent {
    object NavigationAppointmentSchedule: ScheduleListEvent()

    data class NavigationDetail(val id: Int): ScheduleListEvent()
    data class NavigationDetailReceive(val id: Int): ScheduleListEvent()
    data class NavigationDetailRefuse(val id: Int, val reason: String): ScheduleListEvent()
}