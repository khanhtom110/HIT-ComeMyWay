package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class ClinicPostDetailResponse (
    val id: Int,
    val clinicId: Int,
    val clinicName: String,
    val clinicAvatarUrl: String?,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val imageUrls: List<String>
)