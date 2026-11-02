package com.vetpet.petbeats.data.remote.dto.home_clinic.response

data class ClinicPostResponseNodeJs (
    val id: Int,
    val clinicId: Int,
    val clinicName: String,
    val title: String,
    val content: String,
    val imageUrls: List<String?>,
    val clinicThumbnailUrl: String?,
    val status: String?,
    val approvedBy: Int?,
    val approveAt: String?,
    val createAt: String?,
)