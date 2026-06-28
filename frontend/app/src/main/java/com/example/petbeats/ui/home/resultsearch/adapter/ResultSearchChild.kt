package com.example.petbeats.ui.home.resultsearch.adapter

import com.example.petbeats.ui.home.book.adapter.BookChildState

data class ResultSearchChild (
    val id: String,
    val roomName: String,
    val image: Int,
    val action: String, //hoạt động
    val distance: Double, //khoảng cách
    val rating: Float,
    val address: String,
    val time: String,
    val status: ResultSearchChildState
)