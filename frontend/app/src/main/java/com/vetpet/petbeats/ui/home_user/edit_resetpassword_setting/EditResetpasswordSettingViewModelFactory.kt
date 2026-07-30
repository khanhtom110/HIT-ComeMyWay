package com.vetpet.petbeats.ui.home_user.edit_resetpassword_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.AuthRepository

class EditResetpasswordSettingViewModelFactory(
    private val repository: AuthRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditResetpasswordSettingViewModel(repository) as T
    }
}