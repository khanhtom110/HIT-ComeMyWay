package com.example.petbeats.ui.home.search

sealed class SearchEvent {
    object NavigationBook: SearchEvent()
    data class NavigationResultSearch(val search: String): SearchEvent()
    data class NavigationInformationId(val id: Int): SearchEvent()
}