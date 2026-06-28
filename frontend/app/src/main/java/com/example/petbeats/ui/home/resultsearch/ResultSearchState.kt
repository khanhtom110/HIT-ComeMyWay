package com.example.petbeats.ui.home.resultsearch

import com.example.petbeats.ui.home.book.adapter.BookChild
import com.example.petbeats.ui.home.resultsearch.adapter.ResultSearchChild

data class ResultSearchState (
    val list: List<ResultSearchChild> = emptyList()
)