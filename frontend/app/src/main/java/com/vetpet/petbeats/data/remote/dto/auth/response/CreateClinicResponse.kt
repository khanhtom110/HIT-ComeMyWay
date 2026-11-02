package com.vetpet.petbeats.data.remote.dto.auth.response

data class CreateClinicResponse (
    val id: Int,
    val username: String,
    val email: String,
    val status: String
)