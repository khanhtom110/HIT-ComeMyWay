package com.vetpet.petbeats.ui.home_clinic.edit_information_clinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.informationclinic.InformationClinicViewModel

class EditInformationClinicViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditInformationClinicViewModel(repository) as T
    }
}