package com.example.petbeats.ui.home.historylistall

import com.example.petbeats.ui.home.search.SearchEvent

sealed class HistoryListAllEvent {
    object NavigationSearch: HistoryListAllEvent()
    data class NavigationResultSearch(val search: String): HistoryListAllEvent()
}