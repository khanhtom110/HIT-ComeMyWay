package com.vetpet.petbeats.ui.home_clinic.informationclinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class InformationClinicViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InformationClinicViewModel(repository) as T
    }
}