package com.vetpet.petbeats.ui.home_clinic.schedulelist

import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentChild

data class ScheduleListState (
    val isWait: Boolean = true,
    val isReceive: Boolean = false,
    val isRefuse: Boolean = false,


    val listAppointmentChild: List<AppointmentChild> = emptyList()
)