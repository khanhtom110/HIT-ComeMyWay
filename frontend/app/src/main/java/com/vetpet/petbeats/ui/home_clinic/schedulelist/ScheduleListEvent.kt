package com.vetpet.petbeats.ui.home_clinic.schedulelist

sealed class ScheduleListEvent {
    object NavigationAppointmentSchedule: ScheduleListEvent()
}