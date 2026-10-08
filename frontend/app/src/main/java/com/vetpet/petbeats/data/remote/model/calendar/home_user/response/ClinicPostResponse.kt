package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class ClinicPostResponse (
    val id: Int,
    val clinicId: Int,
    val clinicName: String,
    val clinicAvatarUrl: String?,
    val title: String,
    val excerpt: String?,
    val imageUrl: String?,
)