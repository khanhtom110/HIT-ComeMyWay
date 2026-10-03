package com.vetpet.petbeats.ui.home_user.news_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailUserViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(NewsDetailUserState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsDetailUserEvent>()
    val event = _event.asSharedFlow()


    init {
        onNewsDetailClick()
    }


    fun newsClick() {
        viewModelScope.launch {
            _event.emit(NewsDetailUserEvent.NavigationNewsUserClick)
        }
    }

    fun onNewsDetailClick() {
        viewModelScope.launch {

        }
    }

}