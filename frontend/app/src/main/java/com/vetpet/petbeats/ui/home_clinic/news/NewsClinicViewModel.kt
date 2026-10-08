package com.vetpet.petbeats.ui.home_clinic.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.TakeBookingRequest
import com.vetpet.petbeats.data.repository.repository_nodejs.ClinicPostRepository
import com.vetpet.petbeats.data.repository.repository_springboot.HomeUserRepository
import com.vetpet.petbeats.ui.home_clinic.news.adapter.NewsChildClinic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsClinicViewModel @Inject constructor(
    private val repositoryClinic: ClinicPostRepository,
): ViewModel() {
    private val _state = MutableStateFlow(NewsClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsClinicEvent>()
    val event = _event.asSharedFlow()


    init {
        _state.value = _state.value.copy(isLoading = true, listNews = emptyList())

        onNewsList()
    }

    fun itemNewsPostClick(id: Int) {
        viewModelScope.launch {
            _event.emit(NewsClinicEvent.NavigationClinicPost(id))
        }
    }

    fun onNewsList() {
        viewModelScope.launch {
            val result = repositoryClinic.clinicGetNodeJS()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        NewsChildClinic(
                            id = list.id,
                            clinicId = list.clinicId,
                            clinicName = list.clinicName,
                            title = list.title,
                            content = list.content,
                            imageUrls = list.imageUrls.firstOrNull().orEmpty(),
                            imageClinic = list.clinicThumbnailUrl.orEmpty(),
                            status = list.status.orEmpty(),
                            approveBy = list.approvedBy,
                            approveAt = list.approveAt.orEmpty(),
                            createAt = list.createAt.orEmpty()
                        )
                    }

                    _state.value = _state.value.copy(isLoading = false, listNews = showList)
                }
                is DataResult.Error -> {
                    Log.d("tesst", "error: ${result.message}")

                    _state.value = _state.value.copy(isLoading = false, listNews = emptyList())
                }
            }
        }
    }
}