package com.vetpet.petbeats.ui.home_user.book

import com.vetpet.petbeats.ui.home_user.book.adapter.BookChild

data class BookState (
    val isLoading: Boolean = false,

    val listBook: List<BookChild> = emptyList()
)