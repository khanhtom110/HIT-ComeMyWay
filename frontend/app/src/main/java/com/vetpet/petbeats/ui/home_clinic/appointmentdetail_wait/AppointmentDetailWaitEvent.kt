package com.vetpet.petbeats.ui.home_clinic.appointmentdetail_wait

import com.vetpet.petbeats.ui.home_clinic.appointmentdetail_receive.AppointmentDetailReceiveEvent
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.AppointmentScheduleEvent

sealed class AppointmentDetailWaitEvent {
    object NavigationAppointmentSchedule: AppointmentDetailWaitEvent()
    data class NavigationDetailReceive(val id: Int): AppointmentDetailWaitEvent()
    data class NavigationDetailRefuse(val id: Int): AppointmentDetailWaitEvent()
}