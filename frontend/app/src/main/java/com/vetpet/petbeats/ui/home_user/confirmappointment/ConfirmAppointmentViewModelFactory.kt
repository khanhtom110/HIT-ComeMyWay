package com.vetpet.petbeats.ui.home_user.confirmappointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeRepository

class ConfirmAppointmentViewModelFactory(
    private val repository: HomeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConfirmAppointmentViewModel(repository) as T
    }
}