package com.vetpet.petbeats.ui.home_clinic.schedulelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class ScheduleListViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScheduleListViewModel(repository) as T
    }
}