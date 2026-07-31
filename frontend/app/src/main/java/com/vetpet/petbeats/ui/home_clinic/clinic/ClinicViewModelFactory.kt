package com.vetpet.petbeats.ui.home_clinic.clinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeClinicRepository

class ClinicViewModelFactory(
    private val repository: HomeClinicRepository,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClinicViewModel(repository, tokenManager) as T
    }
}