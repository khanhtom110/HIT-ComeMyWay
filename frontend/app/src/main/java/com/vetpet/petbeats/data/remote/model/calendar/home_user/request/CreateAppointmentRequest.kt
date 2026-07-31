package com.vetpet.petbeats.data.remote.model.calendar.home_user.request

data class CreateAppointmentRequest (
    val clinicId:Int,
    val fullName: String,
    val phone: String,
    val bookingType: String,
    val homeAddress: String,
    val petQuantity: Int,
    val petType: String,
    val petCondition: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val serviceIds: List<Int> = emptyList()
)