package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class LocationResponse (
    val id: Int,
    val name: String,
    val address: String,
    val thumbnailUrl: String,
    val distance: Double,
    val rating: Double
)