package com.vetpet.petbeats.data.remote.dto.calendar.home_user.request

data class SearchRequest (
    val keyword: String,
    val latitude: Double?,
    val longitude: Double?
)