package com.vetpet.petbeats.data.remote.dto.home_user.response

data class CreateAppointmentResponse (
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
    val services: List<com.vetpet.petbeats.data.remote.dto.home_user.response.ServiceItem>,
    val status: String
)