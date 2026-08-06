package com.vetpet.petbeats.ui.home_clinic.edit_password_clinic

sealed class EditPasswordClinicEvent {
    object NavigationForgotPassword: EditPasswordClinicEvent()
    object NavigationClinic: EditPasswordClinicEvent()
}