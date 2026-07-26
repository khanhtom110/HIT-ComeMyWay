package com.vetpet.petbeats.data.remote.model.calendar.home_user.request

data class UpdateProfileRequest (
    val fullName: String,
    val phone: String,
    val homeAddress: String,
    val email: String,
    val avatar: String,
    val hobby: String
)