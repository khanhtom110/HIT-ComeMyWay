package com.vetpet.petbeats.ui.home_user.book.adapter

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