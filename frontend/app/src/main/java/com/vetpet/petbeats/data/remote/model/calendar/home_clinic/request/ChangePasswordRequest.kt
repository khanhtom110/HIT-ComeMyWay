package com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request

data class ChangePasswordRequest (
    val password: String,
    val confirmPassword: String
)