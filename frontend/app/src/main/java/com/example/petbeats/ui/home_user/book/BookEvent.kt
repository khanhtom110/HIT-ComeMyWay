package com.example.petbeats.ui.home_user.book

sealed class BookEvent {
    object NavigationSearch: BookEvent()
    object NavigationHistoryBook: BookEvent()
    data class NavigationBookingAppointment(val id: Int, val clinicId: Int): BookEvent()
}