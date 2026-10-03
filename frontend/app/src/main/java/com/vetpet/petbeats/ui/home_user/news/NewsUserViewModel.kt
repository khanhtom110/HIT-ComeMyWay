package com.vetpet.petbeats.ui.home_user.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.data.repository.HomeUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        viewModelScope.launch {

        }
    }
}