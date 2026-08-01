package com.vetpet.petbeats.ui.home_user.image_me_locket

import androidx.lifecycle.ViewModel
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageMeLocketViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(ImageMeLocketState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ImageMeLocketEvent>()
    val event = _event.asSharedFlow()
}