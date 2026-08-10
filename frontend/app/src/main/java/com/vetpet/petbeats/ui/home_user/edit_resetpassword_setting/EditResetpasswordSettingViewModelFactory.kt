package com.vetpet.petbeats.ui.home_user.edit_resetpassword_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.data.repository.HomeUserRepository

class EditResetpasswordSettingViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditResetpasswordSettingViewModel(repository) as T
    }
}