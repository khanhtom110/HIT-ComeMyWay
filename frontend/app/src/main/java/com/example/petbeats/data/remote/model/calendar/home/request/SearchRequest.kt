package com.example.petbeats.data.remote.model.calendar.home.request

data class SearchRequest (
    val keyword: String,
    val latitude: Double,
    val longitude: Double
)