package com.vetpet.petbeats.ui.home_user.image_me_locket

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

class ImageMeLocketViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(ImageMeLocketState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ImageMeLocketEvent>()
    val event = _event.asSharedFlow()


    fun imageMeLocketClick() {
        viewModelScope.launch {
            _event.emit(ImageMeLocketEvent.NavigationImageMeLocket)
        }
    }


    fun onMeLocketList() {
        val lastPostId = _state.value.lastPostId

        viewModelScope.launch {
            val result = repository.getMyPostLocket(lastPostId, 10)

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
                            userName = "Bạn"
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

    fun onMeCancel(postId: Int?) {
//        viewModelScope.launch {
//            val request = CancelLocketRequest(postId)
//            val result = repository.cancelPostLocket(request)
//
//            when (result) {
//                is DataResult.Success -> {
//                    val currentList = _state.value.listMeLocket.toMutableList()
//                    currentList.removeAll {
//                        it.lastPostId == postId
//                    }
//
//                    _state.value = _state.value.copy(listMeLocket = currentList)
//                }
//
//                is DataResult.Error -> {
//                    Log.d("TEST_IMAGE", "Lỗi xóa bài: ${result.message}")
//                    return@launch
//                }
//            }
//        }
    }


}