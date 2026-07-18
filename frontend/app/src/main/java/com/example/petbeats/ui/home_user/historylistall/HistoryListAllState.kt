package com.example.petbeats.ui.home_user.historylistall

import com.example.petbeats.ui.home_user.search.adapterhistory.HistoryChild

data class HistoryListAllState (
    val search: String = "",
    val isSearch: Boolean = false,
    val listHistory: List<HistoryChild> = emptyList()
)