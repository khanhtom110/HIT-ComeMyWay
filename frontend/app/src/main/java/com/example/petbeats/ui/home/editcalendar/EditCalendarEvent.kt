package com.example.petbeats.ui.home.editcalendar

import com.example.petbeats.ui.home.calendar.CalendarEvent

sealed class EditCalendarEvent {
    object NavigationNextRoom: EditCalendarEvent()
    data class NavigationConfirmAppointment(val id: Int, val clinicId: Int): EditCalendarEvent()
    data class NavigationSuccessAppointment(val id: Int, val clinicId: Int): EditCalendarEvent()
}