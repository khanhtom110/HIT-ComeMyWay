package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class AppointmentScheduleViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentScheduleViewModel(repository) as T
    }
}