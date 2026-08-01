package com.vetpet.petbeats.ui.home_user.image_everybody_locket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.image_me_locket.ImageMeLocketViewModel

class ImageEverybodyLocketViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImageMeLocketViewModel(repository) as T
    }
}