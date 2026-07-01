package com.example.petbeats.ui.home.informationroom

import android.accessibilityservice.GestureDescription

data class InformationRoomState (
    val thumbnailUrl: String = "",
    val name: String = "",
    val isOperating: Boolean = false,
    val rating: Double = 0.0,
    val address: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val description: String = "",
    val phone: String = "",
    val services: List<String> = emptyList()
)