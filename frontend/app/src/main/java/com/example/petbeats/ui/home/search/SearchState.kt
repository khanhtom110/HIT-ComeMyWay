package com.example.petbeats.ui.home.search

import com.example.petbeats.ui.home.search.adapterhint.HintChild
import com.example.petbeats.ui.home.search.adapterhistory.HistoryChild

data class SearchState (
    val search: String = "",

    val isSearch: Boolean = false,

    val isButtonAll: Boolean = false,

    val listHint: List<HintChild> = emptyList(),
    val listHistory: List<HistoryChild> = emptyList()
)