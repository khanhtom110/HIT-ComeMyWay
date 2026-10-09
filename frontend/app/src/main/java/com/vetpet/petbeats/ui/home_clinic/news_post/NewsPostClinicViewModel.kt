package com.vetpet.petbeats.ui.home_clinic.news_post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ClinicPostRequestNodeJs
import com.vetpet.petbeats.data.repository.repository_nodejs.ClinicPostRepository
import com.vetpet.petbeats.data.repository.repository_springboot.HomeUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class NewsPostClinicViewModel @Inject constructor(
    private val repositoryClinicPost: ClinicPostRepository,
    private val repositoryUser: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(NewsPostClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsPostClinicEvent>()
    val event = _event.asSharedFlow()

    fun newsClinicClick() {
        viewModelScope.launch {
            _event.emit(NewsPostClinicEvent.NavigationNewsClinic)
        }
    }



    fun checkImageClickFalse() {
        _state.value = _state.value.copy(isImageNews = false)
    }



    fun onTitleChange(title: String) {
        _state.value = _state.value.copy(title = title, isTitle = false)
    }
    fun onContentChange(content: String) {
        _state.value = _state.value.copy(content = content, isContent = false)
    }


    fun onUploadImage(file: MultipartBody.Part) {
        viewModelScope.launch {
            val result = repositoryUser.onUploadImage(file)

            when(result) {
                is DataResult.Success -> {
                    val imageUrl = result.data
                    Log.d("UPLOAD_SUCCESS", "URL ảnh nhận từ Server: $imageUrl")

                    _state.value = _state.value.copy(imageNews = imageUrl, isImageNews = true)
                }
                is DataResult.Error -> {
                    Log.e("UPLOAD_ERROR", "Upload ảnh thất bại: ${result.message}")
                    return@launch
                }
            }
        }
    }


    fun onImageClinic(id: Int) {
        viewModelScope.launch {
            val result = repositoryClinicPost.clinicPostIdNodeJS(id)


            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        imageClinic = data.clinicThumbnailUrl.orEmpty(),
                        nameClinic = data.clinicName
                    )
                }
                is DataResult.Error -> {
                    Log.d("tessst", "error = ${result.message}")

                    return@launch
                }
            }
        }
    }



    fun onNewsPostClinicClick(id: Int) {
        viewModelScope.launch {
            val title = _state.value.title
            val content = _state.value.content
            val imageUrl = _state.value.imageNews

            val imageUrls = if (imageUrl.isNotBlank()) {
                listOf(imageUrl)
            } else {
                emptyList()
            }

            val request = ClinicPostRequestNodeJs(title, content, imageUrls)
            val result = repositoryClinicPost.clinicPostNodeJS(request)

            when (result) {
                is DataResult.Success -> {
                    val newPostId = result.data.id

                    _event.emit(NewsPostClinicEvent.NavigationNewsSuccessClinic(newPostId))
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }
}