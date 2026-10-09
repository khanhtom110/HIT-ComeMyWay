package com.vetpet.petbeats.data.remote.model.calendar.auth.response

data class CreateClinicResponse (
    val id: Int,
    val username: String,
    val email: String,
    val status: String
)