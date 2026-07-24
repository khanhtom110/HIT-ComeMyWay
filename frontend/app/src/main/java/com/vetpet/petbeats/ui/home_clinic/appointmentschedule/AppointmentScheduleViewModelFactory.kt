package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.data.repository.HomeUserRepository

class AppointmentScheduleViewModelFactory(
    private val repositoryClinic: HomeClinicRepository,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentScheduleViewModel(repositoryClinic) as T
    }
}