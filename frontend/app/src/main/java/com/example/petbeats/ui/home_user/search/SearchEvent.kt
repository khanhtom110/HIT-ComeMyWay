package com.example.petbeats.ui.home_user.search

sealed class SearchEvent {
    object NavigationBook: SearchEvent()
    object NavigationHistoryListALl: SearchEvent()
    data class NavigationResultSearch(val search: String): SearchEvent()
    data class NavigationInformationId(val id: Int): SearchEvent()
}