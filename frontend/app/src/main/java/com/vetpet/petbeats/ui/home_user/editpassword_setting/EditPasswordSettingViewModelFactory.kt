package com.vetpet.petbeats.ui.home_user.editpassword_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeUserRepository

class EditPasswordSettingViewModelFactory(
    private val repository: HomeUserRepository,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditPasswordSettingViewModel(repository, tokenManager) as T
    }
}