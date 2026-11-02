package com.vetpet.petbeats.data.remote.dto.home_admin.response

data class CreateClinicChildResponse (
    val content: List<CreateClinicResponse>,
    val hasNext: Boolean,
    val nextCursor: Int
)