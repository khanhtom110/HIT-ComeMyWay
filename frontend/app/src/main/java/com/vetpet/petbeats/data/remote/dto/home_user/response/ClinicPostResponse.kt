package com.vetpet.petbeats.data.remote.dto.home_user.response

data class ClinicPostResponse (
    val id: Int,
    val clinicId: Int,
    val clinicName: String,
    val clinicAvatarUrl: String?,
    val title: String,
    val excerpt: String?,
    val imageUrl: String?,
)