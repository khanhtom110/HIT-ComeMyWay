package com.example.petbeats.ui.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.repository.AuthRepository

class ForgotPasswordViewModelFactory(
    private val repository: AuthRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForgotPasswordViewModel(repository) as T
    }
}