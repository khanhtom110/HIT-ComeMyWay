package com.vetpet.petbeats.ui.home_clinic.scheduletoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class ScheduleTodayViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScheduleTodayViewModel(repository) as T
    }
}