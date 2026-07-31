package com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response

data class GetClinicProfileResponse (
    val id: Int,
    val thumbnailUrl: String,
    val name: String,
    val description: String,
    val phone: String,
    val address: String,
    val mapLink: String,
    val latitude: Double?,
    val longitude: Double?,
    val openTime: String,
    val closeTime: String,
    val services: List<String> = emptyList(),
)