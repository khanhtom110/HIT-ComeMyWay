package com.vetpet.petbeats.ui.home_user.historyBook

import com.vetpet.petbeats.ui.home_user.book.adapter.BookChild

data class HistoryBookState (
    val listBook: List<BookChild> = emptyList()
)