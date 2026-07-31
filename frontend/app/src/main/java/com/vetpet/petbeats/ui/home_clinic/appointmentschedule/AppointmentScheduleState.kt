package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentChild

data class AppointmentScheduleState (
    val isWait: Boolean = true,
    val isReceive: Boolean = false,
    val isRefuse: Boolean = false,


    val listAppointmentChild: List<AppointmentChild> = emptyList()
)