package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class TakeBookingResponse (
    val id: Int,
    val thumbnailUrl: String,
    val name: String,
    val phone: String,
    val address: String,
    val isOperating: Boolean,
    val rating: Double,
    val services: List<ServiceItem> = emptyList()
)


