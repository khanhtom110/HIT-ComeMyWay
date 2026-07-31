package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class AppointmentIdResponse (
    val id: Int,
    val userId: Int,
    val clinicId: Int,
    val fullName: String,
    val phone: String,
    val bookingType: String,
    val homeAddress: String,
    val petType: String,
    val petCondition: String,
    val petQuantity: Int,
    val appointmentDate: String,
    val appointmentTime: String,
    val services: List<ServiceItem>,
    val status: String,
    val rejectReason: String
)