package com.vetpet.petbeats.data.remote.model.calendar.home_clinic.response

data class ChangePasswordResponse (
    val id: Int,
    val username: String,
    val email: String,
    val status: String
)