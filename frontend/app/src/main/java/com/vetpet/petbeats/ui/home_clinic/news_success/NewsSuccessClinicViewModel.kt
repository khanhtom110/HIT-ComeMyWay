package com.vetpet.petbeats.ui.home_clinic.news_success

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.repository_nodejs.ClinicPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsSuccessClinicViewModel @Inject constructor(
    private val repository: ClinicPostRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NewsSuccessClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsSuccessClinicEvent>()
    val event = _event.asSharedFlow()


    fun newsPostClick() {
        viewModelScope.launch {
            _event.emit(NewsSuccessClinicEvent.NavigationNewsPost)
        }
    }
    fun newsClick() {
        viewModelScope.launch {
            _event.emit(NewsSuccessClinicEvent.NavigationNews)
        }
    }


    fun onInformationList(id: Int) {
        viewModelScope.launch {
            val result = repository.clinicPostIdNodeJS(id)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        tvTitle = data.title,
                        tvContent = data.content,
                        imgNews = data.imageUrls.firstOrNull().orEmpty(),
                        imgClinic = data.clinicThumbnailUrl.orEmpty(),
                        checkState = true,
                        checkTry = true
                    )
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(checkState = false, checkTry = false)
                    return@launch
                }
            }
        }
    }



}