package com.example.petbeats.data.remote.model.calendar.home.response

data class ClinicIdResponse (
    val clinicId: Int,
    val thumbnailUrl: String,
    val name: String,
    val isOperating: Boolean,
    val rating: Double,
    val description: String,
    val phone: String,
    val address: String,
    val openTime: String,
    val closeTime: String,
    val services: List<String> = emptyList()
)