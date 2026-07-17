package com.example.petbeats.data.remote.model.calendar.home.response

import com.google.android.gms.common.api.Status

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