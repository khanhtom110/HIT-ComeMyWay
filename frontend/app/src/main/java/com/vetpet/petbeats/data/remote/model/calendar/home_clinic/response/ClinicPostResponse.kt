package com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response

data class ClinicPostResponse(
    val id: Long,
    val clinicId: Long,
    val clinicName: String,
    val title: String,
    val content: String,
    val createdAt: String
)
