package com.example.petbeats.ui.home.book.adapter

data class BookChild (
    val id: Int,
    val clinicId: Int,
    val thumbnailUrl: String,
    val nameClinic: String,
    val address: String,
    val calendar: String,
    val time: String,
    val status: BookChildState
)