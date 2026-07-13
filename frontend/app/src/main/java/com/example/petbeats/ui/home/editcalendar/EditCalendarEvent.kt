package com.example.petbeats.ui.home.editcalendar

sealed class EditCalendarEvent {
    data class NavigationConfirmAppointment(val id: Int, val clinicId: Int): EditCalendarEvent()
    data class NavigationSuccessAppointment(val id: Int): EditCalendarEvent()
}