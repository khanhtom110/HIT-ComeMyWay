package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class ClinicPostConstainResponse (
    val content: List<ClinicPostResponse>,
    val hasNext: Boolean,
    val lastPostId: Int,
)