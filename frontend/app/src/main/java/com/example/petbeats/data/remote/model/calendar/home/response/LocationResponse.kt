package com.example.petbeats.data.remote.model.calendar.home.response

data class LocationResponse (
    val id: Int,
    val name: String,
    val address: String,
    val thumbnailUrl: String,
    val distance: Double,
    val rating: Double
)