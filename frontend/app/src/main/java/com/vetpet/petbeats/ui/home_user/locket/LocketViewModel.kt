package com.vetpet.petbeats.ui.home_user.locket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.CreatePostLocketRequest
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

    fun resetSendSuccess() {
        _state.value = _state.value.copy(
            isSendSuccess = false,
            isLoading = false,
            message = ""
        )
    }


    fun uploadAndSendLocket(imageFile: File) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile)
            val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

            val result = repository.onUploadImage(imagePart)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(linkImage = result.data)

                    val message = _state.value.message
                    val request = CreatePostLocketRequest(result.data, message)

                    val sendResult = repository.createPostLocket(request)

                    when (sendResult) {
                        is DataResult.Success -> {
                            _state.value = _state.value.copy(isLoading = false, isSendSuccess = true, message = "")
                        }
                        is DataResult.Error -> {
                            Log.d("TEST_IMAGE", "Lỗi gửi bài: ${sendResult.message}")
                            _state.value = _state.value.copy(isLoading = false)
                            return@launch
                        }
                    }
                }
                is DataResult.Error -> {
                    Log.d("TEST_IMAGE", "Lỗi up ảnh: ${result.message}")

                    _state.value = _state.value.copy(isLoading = false)
                    return@launch
                }
            }
        }
    }

}