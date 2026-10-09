package com.vetpet.petbeats.ui.home_admin.clinic_detail_admin

data class ClinicDetailAdminState (
    val id: Int = 0,
    val imgClinic: String = "",
    val nameClinic: String = "",
    val phone: String = "",
    val address: String = "",
    val map: String = "",
    val closeTime: String = "",
    val openTime: String = "",
    val description: String = "",
    val services: List<String> = emptyList(),

    val latitude: Double? = null,
    val longitude: Double? = null
)