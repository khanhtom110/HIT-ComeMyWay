package com.vetpet.petbeats.ui.home_admin.add_clinic_admin

data class AddClinicAdminState (
    val name: String = "",
    val email: String = "",


    val isName: Boolean = false,
    val isEmail: Boolean = false,
    val nameError: String = "",
    val emailError: String = "",
)