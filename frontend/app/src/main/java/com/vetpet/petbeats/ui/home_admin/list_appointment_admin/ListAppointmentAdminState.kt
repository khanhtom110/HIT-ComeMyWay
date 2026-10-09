package com.vetpet.petbeats.ui.home_admin.list_appointment_admin

import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.adapter.AppointmentAdminChild

data class ListAppointmentAdminState (
    val isReceive: Boolean = false,
    val isWait: Boolean = false,
    val isLoading: Boolean = false,

    val hasNext: Boolean = true,
    val nextCursor: Int? = null,


    val listAppointmentAdmin: List<AppointmentAdminChild> = emptyList()
)