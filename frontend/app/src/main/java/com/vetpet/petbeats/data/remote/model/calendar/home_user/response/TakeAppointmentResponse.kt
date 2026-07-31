package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class TakeAppointmentResponse (
    val id: Int,
    val clinicId: Int,
    val name: String,
    val address: String,
    val thumbnailUrl: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val status: String
)