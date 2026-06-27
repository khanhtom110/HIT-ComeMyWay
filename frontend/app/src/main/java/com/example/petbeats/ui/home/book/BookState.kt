package com.example.petbeats.ui.home.book

import com.example.petbeats.ui.home.book.adapter.BookChild

data class BookState (
    val list: List<BookChild> = emptyList()
)