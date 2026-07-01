package com.example.petbeats.ui.home.search

import com.example.petbeats.ui.home.search.adapterhint.HintChild
import com.example.petbeats.ui.home.search.adapterhistory.HistoryChild

data class SearchState (
    val search: String = "",

    val isSearch: Boolean = false,

    val isButtonAll: Boolean = false,

    //history
    val listHistory: List<HistoryChild> = emptyList(),
    //hint
    val listHint: List<HintChild> = emptyList(),
    //result_search
    val listSearch: List<HintChild> = emptyList(),

    val latitude: Double? = null,
    val longitude: Double? = null
)