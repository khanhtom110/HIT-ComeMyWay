package com.example.petbeats.ui.home.book

import com.example.petbeats.ui.home.historyBook.HistoryBookEvent

sealed class BookEvent {
    object NavigationSearch: BookEvent()
    object NavigationHistoryBook: BookEvent()
    data class NavigationBookingAppointment(val id: Int, val clinicId: Int): BookEvent()
}