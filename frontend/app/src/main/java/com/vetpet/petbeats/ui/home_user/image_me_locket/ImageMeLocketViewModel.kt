package com.vetpet.petbeats.ui.home_user.image_me_locket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content
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



    fun onMeLocketList() {
        val lastPostId = _state.value.lastPostId

//        viewModelScope.launch {
//            val result = repository.getMyPostLocket(lastPostId, 10)
//
//            when (result) {
//                is DataResult.Success -> {
//                    val apiDataList = result.data
//                    val apiContentList = apiDataList?.content ?: emptyList()
//
//                    val showList = apiContentList.map { list ->
//                        ImageLocketChild(
//                            lastPostId = list.lastPostId,
//                            imageUrl = list.content,
//                            caption = list.content
//                        )
//                    }
//                    _state.value = _state.value.copy(listMeLocket = showList)
//                }
//                is DataResult.Error -> {
//                    _state.value = _state.value.copy(listMeLocket = emptyList())
//                }
//            }
//        }
    }
}