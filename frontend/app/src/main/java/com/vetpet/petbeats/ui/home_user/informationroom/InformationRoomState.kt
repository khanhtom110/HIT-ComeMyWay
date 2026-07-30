package com.vetpet.petbeats.ui.home_user.informationroom

data class InformationRoomState (
    val id: Int = 0,
    val thumbnailUrl: String = "",
    val name: String = "",
    val isOperating: Boolean = false,
    val rating: Double = 0.0,
    val address: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val description: String = "",
    val phone: String = "",
    val services: List<String> = emptyList(),
    val mapLink: String = "",
    val distance: Double = 0.0,

    val latitude: Double? = null,
    val longitude: Double? = null
)