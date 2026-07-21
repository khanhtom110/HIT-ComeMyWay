package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

sealed class AppointmentScheduleEvent {
    object NavigationScheduleToday: AppointmentScheduleEvent()
    object NavigationScheduleList: AppointmentScheduleEvent()
}