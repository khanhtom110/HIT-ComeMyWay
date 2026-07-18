package com.example.petbeats.ui.home_user.search

import com.example.petbeats.ui.home_user.search.adapterhint.HintChild
import com.example.petbeats.ui.home_user.search.adapterhistory.HistoryChild

data class SearchState (
    val search: String = "",

    val isSearch: Boolean = false,

    val isButtonAll: Boolean = false,

    //history
    val listHistory: List<HistoryChild> = emptyList(),
    //hint
    val listHint: List<HintChild> = emptyList(),

    val latitude: Double? = null,
    val longitude: Double? = null
)