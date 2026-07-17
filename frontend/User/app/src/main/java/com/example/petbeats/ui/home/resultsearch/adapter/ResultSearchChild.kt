package com.example.petbeats.ui.home.resultsearch.adapter

import com.example.petbeats.ui.home.book.adapter.BookChildState

data class ResultSearchChild (
    val id: Int,
    val roomName: String,
    val image: String,
    val isOperating: Boolean,
    val distance: Double, //khoảng cách
    val rating: Double,
    val address: String,
    val closeTime: String,
    val openTime: String,
)