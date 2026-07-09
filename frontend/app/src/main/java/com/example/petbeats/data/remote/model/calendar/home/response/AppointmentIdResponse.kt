package com.example.petbeats.data.remote.model.calendar.home.response

data class AppointmentIdResponse (
    val userId: Int,
    val clinicId: Int,
    val name: String,
    val address: String,
    val thumbnailUrl: String,
    val fullName: String,
    val phone: String,
    val bookingType: String,
    val homeAddress: String,
    val petType: String,
    val petCondition: String,
    val petQuantity: Int,
    val appointmentDate: String,
    val appointmentTime: String,
    val services: List<String>,
    val status: String
)