package com.example.petbeats.ui.home_user.successAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.repository.HomeRepository

class SuccessAppointmentViewModelFactory(
    private val repository: HomeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SuccessAppointmentViewModel(repository) as T
    }
}