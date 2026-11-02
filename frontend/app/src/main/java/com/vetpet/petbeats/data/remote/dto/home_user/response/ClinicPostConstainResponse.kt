package com.vetpet.petbeats.data.remote.dto.home_user.response

data class ClinicPostConstainResponse (
    val content: List<com.vetpet.petbeats.data.remote.dto.home_user.response.ClinicPostResponse>,
    val hasNext: Boolean,
    val lastPostId: Int,
)