package com.vetpet.petbeats.ui.home_user.resultsearch

import com.vetpet.petbeats.ui.home_user.resultsearch.adapter.ResultSearchChild

data class ResultSearchState (
    val listResultSearch: List<ResultSearchChild> = emptyList(),
    val search: String = "",
    val isSearch: Boolean = false,

    val latitude: Double? = null,
    val longitude: Double? = null,
)