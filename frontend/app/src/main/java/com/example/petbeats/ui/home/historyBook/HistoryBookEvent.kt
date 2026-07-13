package com.example.petbeats.ui.home.historyBook

sealed class HistoryBookEvent {
    object NavigationBook: HistoryBookEvent()
    data class NavigationBookingAppointment(val id: Int, val clinicId: Int): HistoryBookEvent()
}