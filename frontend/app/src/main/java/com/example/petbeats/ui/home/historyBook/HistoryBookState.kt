package com.example.petbeats.ui.home.historyBook

import com.example.petbeats.ui.home.book.adapter.BookChild

data class HistoryBookState (
    val listBook: List<BookChild> = emptyList()
)