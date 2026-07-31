package com.vetpet.petbeats.ui.home_clinic.clinic

sealed class ClinicEvent {
    object NavigationEditInformation: ClinicEvent()
    object NavigationEditPassword: ClinicEvent()
    object NavigationLogin: ClinicEvent()
}