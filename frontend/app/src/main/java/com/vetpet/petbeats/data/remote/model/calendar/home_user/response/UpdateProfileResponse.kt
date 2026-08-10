package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class UpdateProfileResponse (
    val id: Int,
    val fullName: String?,
    val phone: String?,
    val homeAddress: String?,
    val email: String,
    val avatar: String?,
    val hobby: String?
)