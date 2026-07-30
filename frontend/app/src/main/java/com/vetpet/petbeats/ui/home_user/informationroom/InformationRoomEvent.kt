package com.vetpet.petbeats.ui.home_user.informationroom

sealed class InformationRoomEvent {
    object NavigationResultSearch: InformationRoomEvent()
    data class NavigationCalendar(val id: Int, val clinicId: Int): InformationRoomEvent()
}