package com.vetpet.petbeats.ui.home_clinic.appointmentdetail_receive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class AppointmentDetailReceiveViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentDetailReceiveViewModelFactory(repository) as T
    }
}