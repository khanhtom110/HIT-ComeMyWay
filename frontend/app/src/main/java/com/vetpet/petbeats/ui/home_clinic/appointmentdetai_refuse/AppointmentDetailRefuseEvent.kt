package com.vetpet.petbeats.ui.home_clinic.appointmentdetai_refuse

import com.vetpet.petbeats.ui.home_clinic.appointmentdetail_receive.AppointmentDetailReceiveEvent

sealed class AppointmentDetailRefuseEvent {
    object NavigationAppointmentSchedule: AppointmentDetailRefuseEvent()
}