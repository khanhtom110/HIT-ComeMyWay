package com.vetpet.petbeats.ui.home_admin.clinic_detail_admin

import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.ListAppointmentAdminEvent

sealed class ClinicDetailAdminEvent {
    object NavigationListAppointment: ClinicDetailAdminEvent()
}