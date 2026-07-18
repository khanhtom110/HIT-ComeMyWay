package com.example.petbeats.ui.home_user.resultsearch

sealed class ResultSearchEvent {
    object NavigationSearch: ResultSearchEvent()
    data class NavigationInformation(val id: Int) : ResultSearchEvent()
}