package com.example.petbeats.ui.home.book

import com.example.petbeats.ui.home.book.adapter.BookChild

data class BookState (
    val listBook: List<BookChild> = emptyList()
)