package com.example.petbeats.data.remote.model.calendar.home.response

data class SearchResponse (
    val id: Int,
    val thumbnailUrl: String,
    val name: String,
    val isOperating: Boolean,
    val rating: Double,
    val distance: Double,
    val address: String,
    val openTime: String,
    val closeTime: String
)