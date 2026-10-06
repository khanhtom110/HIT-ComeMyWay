package com.vetpet.petbeats.ui.home_admin.list_appointment_admin

import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.adapter.AppointmentAdminChild

data class ListAppointmentAdminState (
    val isReceive: Boolean = false,
    val isWait: Boolean = false,
    val isLoading: Boolean = false,

    val listAppointmentAdmin: List<AppointmentAdminChild> = emptyList()
)