package com.vetpet.petbeats.ui.home_user.image_everybody_locket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.image_me_locket.adapter.ImageLocketChild
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageEverybodyLocketViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(ImageEverybodyLocketState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ImageEverybodyLocketEvent>()
    val event = _event.asSharedFlow()


    fun imageEverybodyLocketClick() {
        viewModelScope.launch {
            _event.emit(ImageEverybodyLocketEvent.NavigationImageEverybodyLocket)
        }
    }


    fun onMeLocketList() {
        val lastPostId = _state.value.lastPostId

        viewModelScope.launch {
            val result = repository.getMyFeedLocket(lastPostId, 10)

            if (_state.value.isLoading || !_state.value.hasNext) {
                return@launch
            }
            _state.value = _state.value.copy(isLoading = true)

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data
                    val apiContentList = apiDataList.content

                    val showList = apiContentList.map { list ->
                        ImageLocketChild(
                            lastPostId = list.id,
                            imageUrl = list.imageUrl,
                            caption = list.caption,
                            userName = list.username
                        )
                    }

                    val currentList = if (lastPostId == null) emptyList() else _state.value.listMeLocket
                    val updatedList = currentList + showList

                    _state.value = _state.value.copy(listMeLocket = updatedList, lastPostId = apiDataList.lastPostId, hasNext = apiDataList.hasNext)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listMeLocket = emptyList())
                }
            }
        }
    }
}