package com.vetpet.petbeats.ui.home_user.editcalendar

sealed class EditCalendarEvent {
    object NavigationNextRoom: EditCalendarEvent()
    data class NavigationConfirmAppointment(val id: Int, val clinicId: Int): EditCalendarEvent()
    data class NavigationSuccessAppointment(val id: Int, val clinicId: Int): EditCalendarEvent()
}