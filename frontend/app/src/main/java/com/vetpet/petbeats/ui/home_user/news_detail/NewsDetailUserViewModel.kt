package com.vetpet.petbeats.ui.home_user.news_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.repository_springboot.HomeUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailUserViewModel @Inject constructor(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(NewsDetailUserState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsDetailUserEvent>()
    val event = _event.asSharedFlow()


    fun newsClick() {
        viewModelScope.launch {
            _event.emit(NewsDetailUserEvent.NavigationNewsUserClick)
        }
    }

    fun onNewsDetailClick(id: Int) {
        viewModelScope.launch {
            val result = repository.clinicPostDetail(id)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    Log.d("Tesst", "data = $data")
                    Log.d("Tesst", "message = ${result.message}")

                    _state.value = _state.value.copy(
                        id = id,
                        clinicId = data.clinicId,
                        clinicName = data.clinicName,
                        clinicAvatarUrl = data.clinicAvatarUrl.orEmpty(),
                        titleClinic = data.title,
                        content = data.content,
                        imageUrl = data.imageUrl.orEmpty(),
                        imageUrls = data.imageUrls
                    )
                }
                is DataResult.Error -> {
                    Log.d("Tesst", "message = ${result.message}")
                    return@launch
                }
            }
        }
    }

}