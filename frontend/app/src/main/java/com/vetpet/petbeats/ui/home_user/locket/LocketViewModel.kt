package com.vetpet.petbeats.ui.home_user.locket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class LocketViewModel(
    private val repository: HomeUserRepository,
): ViewModel() {
    private val _state = MutableStateFlow(LocketState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LocketEvent>()
    val event = _event.asSharedFlow()



    fun listFriendLocket() {
        viewModelScope.launch {
            _event.emit(LocketEvent.NavigationListFriendLocket)
        }
    }
    fun imageMeLocket() {
        viewModelScope.launch {
            _event.emit(LocketEvent.NavigationMeLocket)
        }
    }
    fun imageEverybodyLocket() {
        viewModelScope.launch {
            _event.emit(LocketEvent.NavigationEverybodyLocket)
        }
    }



    fun downCheckLocketTrue() {
        _state.value = _state.value.copy(isDown = true)
    }
    fun downCheckLocketFalse() {
        _state.value = _state.value.copy(isDown = false)
    }


    fun changeFlash() {
        _state.value = _state.value.copy(isFlash = !_state.value.isFlash)
    }




    fun onFeelChange(message: String) {
        _state.value = _state.value.copy(message = message)
    }



    fun onImageLocketSend(imageFile: File) {
        viewModelScope.launch {
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile)
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val message = _state.value.message





        }
    }

}