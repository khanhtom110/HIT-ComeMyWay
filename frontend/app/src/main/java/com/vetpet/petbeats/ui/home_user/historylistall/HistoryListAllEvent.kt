package com.vetpet.petbeats.ui.home_user.historylistall

sealed class HistoryListAllEvent {
    object NavigationSearch: HistoryListAllEvent()
    data class NavigationResultSearch(val search: String): HistoryListAllEvent()
}