package com.vetpet.petbeats.data.remote.dto.home_clinic.request

data class ChangePasswordRequest (
    val password: String,
    val confirmPassword: String
)