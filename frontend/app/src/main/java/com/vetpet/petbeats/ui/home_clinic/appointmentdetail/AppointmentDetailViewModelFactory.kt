package com.vetpet.petbeats.ui.home_clinic.appointmentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class AppointmentDetailViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentDetailViewModel(repository) as T
    }
}