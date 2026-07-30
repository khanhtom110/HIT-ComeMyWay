package com.vetpet.petbeats.ui.auth.changepassword_clinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class ChangePasswordViewModelFactory(
    private val repository: HomeClinicRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChangePasswordViewModel(repository) as T
    }
}