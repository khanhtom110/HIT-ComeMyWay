package com.example.petbeats.ui.home_user.book

import com.example.petbeats.ui.home_user.book.adapter.BookChild

data class BookState (
    val listBook: List<BookChild> = emptyList()
)