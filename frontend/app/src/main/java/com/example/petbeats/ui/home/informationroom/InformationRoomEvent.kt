package com.example.petbeats.ui.home.informationroom

sealed class InformationRoomEvent {
    object NavigationResultSearch: InformationRoomEvent()
    data class NavigationCalendar(val id: Int, val clinicId: Int): InformationRoomEvent()
}