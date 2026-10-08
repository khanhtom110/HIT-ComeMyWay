package com.vetpet.petbeats.ui.home_user.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.repository_springboot.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.news.adapter.NewsChild
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class NewsUserViewModel @Inject constructor(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(NewsUserState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsUserEvent>()
    val event = _event.asSharedFlow()


    init {
        _state.value = _state.value.copy(isLoading = true, listNews = emptyList())

        onNewsList()
    }

    fun itemClickNews(id: Int) {
        viewModelScope.launch {
            _event.emit(NewsUserEvent.NavigaitonNewsUser(id))
        }
    }



    fun onNewsList() {
        val lastPostId = _state.value.lastPostId

        viewModelScope.launch {
            val result = repository.clinicPost(lastPostId, 10)

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data
                    val apiContentList = apiDataList.content


                    val showList = apiContentList.map { list ->
                        NewsChild(
                            id = list.id,
                            clinic = list.clinicId,
                            clinicName = list.clinicName,
                            clinicAvatarUrl = list.clinicAvatarUrl.orEmpty(),
                            titleClinic = list.title,
                            excerpt = list.excerpt.orEmpty(),
                            imageUrl = list.imageUrl.orEmpty()
                        )
                    }

                    val currentList = if (lastPostId == null) emptyList() else _state.value.listNews
                    val updatedList = currentList + showList

                    _state.value = _state.value.copy(isLoading = false, listNews = updatedList, lastPostId = apiDataList.lastPostId, hasNext = apiDataList.hasNext)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, listNews = emptyList())
                }
            }
        }
    }
}