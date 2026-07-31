package com.vetpet.petbeats.ui.home_clinic.appointmentdetai_refuse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class AppointmentDetailRefuseViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentDetailRefuseViewModel(repository) as T
    }
}