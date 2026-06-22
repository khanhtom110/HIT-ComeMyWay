package com.example.petbeats.ui.auth.registersuccess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.repository.AuthRepository

class RegisterSuccessViewModelFactory(
    private val repository: AuthRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterSuccessViewModel(repository) as T
    }
}