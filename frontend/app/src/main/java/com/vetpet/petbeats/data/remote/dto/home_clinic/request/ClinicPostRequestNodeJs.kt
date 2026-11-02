package com.vetpet.petbeats.data.remote.dto.home_clinic.request

data class ClinicPostRequestNodeJs (
    val title: String,
    val content: String,
    val imageUrls: List<String>
)