package com.vetpet.petbeats.data.remote.model.calendar.home_user.request

data class ChangePasswordUserRequest (
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)