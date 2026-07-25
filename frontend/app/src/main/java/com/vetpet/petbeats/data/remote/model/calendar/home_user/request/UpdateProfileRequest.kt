package com.vetpet.petbeats.data.remote.model.calendar.home_user.request

data class UpdateProfileRequest (
    val email: String,
    val avatar: String,
    val hobby: String
)