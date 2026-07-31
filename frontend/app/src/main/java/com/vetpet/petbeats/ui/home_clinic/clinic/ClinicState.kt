package com.vetpet.petbeats.ui.home_clinic.clinic

data class ClinicState (
    val id: Int = 0,
    val image: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val link: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val state: String = "",
    val services: List<String> = emptyList()
)