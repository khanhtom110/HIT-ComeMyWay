package com.vetpet.petbeats.data.remote.dto.calendar.home.response

data class LocationResponse (
    val id: Int,
    val name: String,
    val address: String,
    val thumbnailUrl: String,
    val distance: Double,
    val rating: Double
)