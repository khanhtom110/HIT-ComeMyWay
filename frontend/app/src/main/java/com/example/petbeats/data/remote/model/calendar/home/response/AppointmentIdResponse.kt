package com.example.petbeats.data.remote.model.calendar.home.response

data class AppointmentIdResponse (
    val id: Int,
    val userId: Int,
    val clinicId: Int,
    val fullName: String,
    val phoneUser: String,
    val phoneClinic: String,
    val homeAddress: String,
    val petType: String,
    val petCondition: String,
    val petQuantity: Int,
    val appointmentDate: String,
    val appointmentTime: String,
    val services: List<String>,
    val status: String
)