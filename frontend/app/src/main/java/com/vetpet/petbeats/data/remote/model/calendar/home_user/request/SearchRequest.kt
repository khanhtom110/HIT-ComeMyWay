package com.vetpet.petbeats.data.remote.model.calendar.home_user.request

data class SearchRequest (
    val keyword: String,
    val latitude: Double?,
    val longitude: Double?
)