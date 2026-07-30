package com.vetpet.petbeats.ui.home_user.edit_password_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeUserRepository

class EditPasswordSettingViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditPasswordSettingViewModel(repository) as T
    }
}