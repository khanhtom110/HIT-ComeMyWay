package com.example.petbeats.ui.home_user.calendar

sealed class CalendarEvent {
    object NavigationNextRoom: CalendarEvent()

    data class NavigationInformationRoom(val id: Int): CalendarEvent()
    data class NavigationSuccessAppointment(val id: Int, val clinicId: Int): CalendarEvent()
}