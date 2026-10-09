package com.vetpet.petbeats.data.remote.model.calendar.home_admin.response

data class CreateClinicChildResponse (
    val content: List<CreateClinicResponse>,
    val hasNext: Boolean,
    val nextCursor: Int
)