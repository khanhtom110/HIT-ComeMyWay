package com.vetpet.petbeats.ui.home_admin.list_appointment_admin

import com.vetpet.petbeats.ui.home_admin.clinic_detail_admin.ClinicDetailAdminEvent

sealed class ListAppointmentAdminEvent {
    object NavigationAddClinic: ListAppointmentAdminEvent()
    data class NavigationClinicDetail (val clinicId: Int): ListAppointmentAdminEvent()
}