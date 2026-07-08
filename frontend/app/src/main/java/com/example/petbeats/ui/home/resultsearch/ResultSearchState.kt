package com.example.petbeats.ui.home.resultsearch

import com.example.petbeats.ui.home.book.adapter.BookChild
import com.example.petbeats.ui.home.resultsearch.adapter.ResultSearchChild

data class ResultSearchState (
    val listResultSearch: List<ResultSearchChild> = emptyList(),
    val search: String = "",
    val isSearch: Boolean = false,

    val latitude: Double? = null,
    val longitude: Double? = null,
)