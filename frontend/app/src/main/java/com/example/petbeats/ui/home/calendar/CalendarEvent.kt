package com.example.petbeats.ui.home.calendar

sealed class CalendarEvent {
    data class NavigationInformationRoom(val id: Int): CalendarEvent()
}