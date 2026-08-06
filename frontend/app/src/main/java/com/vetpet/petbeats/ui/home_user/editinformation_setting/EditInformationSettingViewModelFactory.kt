package com.vetpet.petbeats.ui.home_user.editinformation_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.editpassword_setting.EditPasswordSettingViewModel

class EditInformationSettingViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditInformationSettingViewModel(repository) as T
    }
}