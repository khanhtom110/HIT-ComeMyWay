package com.example.petbeats.ui.home.calendar

import com.example.petbeats.ui.home.informationroom.InformationRoomEvent

sealed class CalendarEvent {
    data class NavigationInformationRoom(val id: Int): CalendarEvent()
    data class NavigationSuccessAppointment(val id: Int): CalendarEvent()
}